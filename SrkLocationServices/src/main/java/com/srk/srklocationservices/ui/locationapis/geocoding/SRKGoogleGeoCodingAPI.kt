package com.srk.srklocationservices.ui.locationapis.geocoding

import com.srk.srklocationservices.api.RetrofitServiceUtil
import com.srk.srklocationservices.models.placedetails.FormattedPlaceDetailsModel
import com.srk.srklocationservices.models.placedetails.PlaceDetailsResponse
import com.srk.srklocationservices.ui.locationapis.GooglePlacesAPI
import com.srk.srklocationservices.ui.locationapis.SRKLocationBuilder
import com.srk.srklocationservices.utils.AppConstants.GEOCODING
import com.srk.srklocationservices.utils.AppConstants.OK
import com.srk.srklocationservices.utils.AppConstants.PLACE_DETAILS
import com.srk.srklocationservices.utils.AppConstants.SOMETHING_WENT_WRONG
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SRKGoogleGeoCodingAPI(srkLocationBuilder: SRKLocationBuilder) :
    GooglePlacesAPI(srkLocationBuilder) {


    private fun prepareGeoCodingURL(): String {
        val builder: StringBuilder = StringBuilder()
        //Set google API Key
        val key = checkGoogleApiKey()
        if (key.first) {
            builder.append(PARAM_GOOGLE_KEY).append(key.second)
        } else {
            throw IllegalArgumentException(key.second)
        }

        //Set address
        val address = checkAddress()
        if (address.first) {
            builder.append("&").append(PARAM_ADDRESS).append(address.second)
        } else {
            throw IllegalArgumentException(address.second)
        }

        return builder.toString()
    }

    fun getGeoCoding() {
        val listener = srkLocationBuilder.getLocationResultListner()
        listener?.let { onLocationListener ->
            try {
                val finalConstructedURL = prepareGeoCodingURL()
                onLocationListener.onLocationDetailsFetched(loading())
                val response = RetrofitServiceUtil().locationAPIService()
                    .getGeoCoding(GEOCODING.plus(finalConstructedURL))
                response.enqueue(object : Callback<PlaceDetailsResponse?> {
                    override fun onResponse(
                        call: Call<PlaceDetailsResponse?>, response: Response<PlaceDetailsResponse?>
                    ) {
                        onLocationListener.onLocationDetailsFetched(try {
                            if (response.isSuccessful && response.body() != null) {
                                val placeDetailsResponse = response.body() as PlaceDetailsResponse
                                placeDetailsResponse.status?.let {
                                    if (it.equals(OK, true)) {
                                        if (srkLocationBuilder.getNeedResultInFormattedModel()) success(
                                            GEOCODING,
                                            placeDetailsResponse.toFormattedPlaceDetailsModel()
                                        ) else success(
                                            GEOCODING, placeDetailsResponse
                                        )

                                    } else {
                                        failure(GEOCODING,
                                            placeDetailsResponse.error_message ?: run { it })
                                    }
                                } ?: run {
                                    failure(GEOCODING, SOMETHING_WENT_WRONG)
                                }
                            } else if (response.errorBody() != null) {
                                error(GEOCODING, response.errorBody())
                            } else {
                                failure(GEOCODING, SOMETHING_WENT_WRONG)
                            }
                        } catch (e: Exception) {
                            exception(GEOCODING, e.message.toString())
                        })
                    }

                    override fun onFailure(call: Call<PlaceDetailsResponse?>, e: Throwable) {
                        onLocationListener.onLocationDetailsFetched(
                            exception(GEOCODING, e.message.toString())
                        )
                    }
                })
            } catch (e: Exception) {
                onLocationListener.onLocationDetailsFetched(
                    exception(GEOCODING, e.message.toString())
                )
            }
        }

    }

    fun PlaceDetailsResponse.toFormattedPlaceDetailsModel(): FormattedPlaceDetailsModel? {
        var formattedPlaceDetailsModel: FormattedPlaceDetailsModel? = null
        this.result?.let { item ->
            formattedPlaceDetailsModel = FormattedPlaceDetailsModel()
            formattedPlaceDetailsModel?.apply {
                placeId = item.placeId
                name = item.vicinity
                fullAddress = item.formattedAddress
                locationLat = item.geometry?.location?.lat
                locationLong = item.geometry?.location?.lng
                item.addressComponents?.let {
                    for (address in it) {
                        val type = address.types?.first()
                        type?.let { typeAddress ->
                            if (typeAddress.equals("locality", true)) {
                                locality = address.longName
                                localityShort = address.shortName
                            } else if (typeAddress.equals("sublocality", true)) {
                                sublocality = address.longName
                                sublocalityShort = address.shortName
                            } else if (typeAddress.equals("administrative_area_level_3", true)) {
                                city = address.longName
                                cityShort = address.shortName
                            } else if (typeAddress.equals("administrative_area_level_2", true)) {
                                country = address.longName
                                countryShort = address.shortName
                            } else if (typeAddress.equals("administrative_area_level_1", true)) {
                                state = address.longName
                                stateShort = address.shortName
                            } else if (typeAddress.equals("country", true)) {
                                country = address.longName
                                countryShort = address.shortName
                            } else if (typeAddress.equals("postal_code", true)) {
                                postal_code = address.longName
                                postal_codeShort = address.shortName
                            }
                        }
                    }
                }
            }
        }
        return formattedPlaceDetailsModel
    }

}