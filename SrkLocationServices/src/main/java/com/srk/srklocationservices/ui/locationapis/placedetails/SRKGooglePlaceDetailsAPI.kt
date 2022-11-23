package com.srk.srklocationservices.ui.locationapis.placedetails

import com.srk.srklocationservices.api.RetrofitServiceUtil
import com.srk.srklocationservices.models.placedetails.FormattedPlaceDetailsModel
import com.srk.srklocationservices.models.placedetails.PlaceDetailsResponse
import com.srk.srklocationservices.ui.locationapis.GooglePlacesAPI
import com.srk.srklocationservices.ui.locationapis.SRKLocationBuilder
import com.srk.srklocationservices.utils.AppConstants.OK
import com.srk.srklocationservices.utils.AppConstants.PLACE_DETAILS
import com.srk.srklocationservices.utils.AppConstants.SOMETHING_WENT_WRONG
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SRKGooglePlaceDetailsAPI(srkLocationBuilder: SRKLocationBuilder) :
    GooglePlacesAPI(srkLocationBuilder) {


    private fun preparePlaceDetailsURL(): String {
        val builder: StringBuilder = StringBuilder()
        //Set google API Key
        val key = checkGoogleApiKey()
        if (key.first) {
            builder.append(PARAM_GOOGLE_KEY).append(key.second)
        } else {
            throw IllegalArgumentException(key.second)
        }

        //Set place id
        val placeId = checkPlaceId()
        if (placeId.first) {
            builder.append("&").append(PARAM_PLACE_ID).append(placeId.second)
        } else {
            throw IllegalArgumentException(placeId.second)
        }
        //Set keyword
        srkLocationBuilder.getFields()?.let {
            builder.append("&").append(PARAM_FIELDS).append(it)
        }
        //Set language
        languageCheck()?.let {
            if (it.first) {
                builder.append("&").append(PARAM_LANGUAGE).append(it.second)
            } else {
                throw IllegalArgumentException(it.second)
            }
        }

        //Set Region
        srkLocationBuilder.getRegion()?.let {
            builder.append("&").append(PARAM_REGION).append(it)
        }

        //Set Reviews No Translations
        srkLocationBuilder.getReviewsNoTranslations()?.let {
            builder.append("&").append(PARAM_REVIEWS_NO_TRANSLATIONS).append(it)
        }

        //Set Reviews sort
        srkLocationBuilder.getReviewsSort()?.let {
            builder.append("&").append(PARAM_REVIEWS_SORT).append(it)
        }

        //Set Session token
        srkLocationBuilder.getSessiontoken()?.let {
            builder.append("&").append(PARAM_SESSIONTOKEN).append(it)
        }

        return builder.toString()
    }

    fun getPlaceDetails() {
        val listener = srkLocationBuilder.getLocationResultListner()
        listener?.let { onLocationListener ->
            try {
                val finalConstructedURL = preparePlaceDetailsURL()
                onLocationListener.onLocationDetailsFetched(loading())
                val response = RetrofitServiceUtil().locationAPIService()
                    .getPlaceDetailsByPlaceId(PLACE_DETAILS.plus(finalConstructedURL))
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
                                            PLACE_DETAILS,
                                            placeDetailsResponse.toFormattedPlaceDetailsModel()
                                        ) else success(
                                            PLACE_DETAILS, placeDetailsResponse
                                        )

                                    } else {
                                        failure(PLACE_DETAILS,
                                            placeDetailsResponse.error_message ?: run { it })
                                    }
                                } ?: run {
                                    failure(PLACE_DETAILS, SOMETHING_WENT_WRONG)
                                }
                            } else if (response.errorBody() != null) {
                                error(PLACE_DETAILS, response.errorBody())
                            } else {
                                failure(PLACE_DETAILS, SOMETHING_WENT_WRONG)
                            }
                        } catch (e: Exception) {
                            exception(PLACE_DETAILS, e.message.toString())
                        })
                    }

                    override fun onFailure(call: Call<PlaceDetailsResponse?>, e: Throwable) {
                        onLocationListener.onLocationDetailsFetched(
                            exception(PLACE_DETAILS, e.message.toString())
                        )
                    }
                })
            } catch (e: Exception) {
                onLocationListener.onLocationDetailsFetched(
                    exception(PLACE_DETAILS, e.message.toString())
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