package com.srk.srklocationservices.ui.locationapis.nearbysearch

import com.srk.srklocationservices.api.RetrofitServiceUtil
import com.srk.srklocationservices.models.nearbyplaces.FormattedNearByPlaceModel
import com.srk.srklocationservices.models.nearbyplaces.NearBySearchResponse
import com.srk.srklocationservices.ui.locationapis.GooglePlacesAPI
import com.srk.srklocationservices.ui.locationapis.SRKLocationBuilder
import com.srk.srklocationservices.utils.AppConstants.NEAR_PLACES
import com.srk.srklocationservices.utils.AppConstants.OK
import com.srk.srklocationservices.utils.AppConstants.SOMETHING_WENT_WRONG
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SRKGoogleNearPlacesAPI(srkLocationBuilder: SRKLocationBuilder) :
    GooglePlacesAPI(srkLocationBuilder) {


    private fun prepareNearByPlacesURL(): String {
        val builder: StringBuilder = StringBuilder()
        //Set google API Key
        val key = checkGoogleApiKey()
        if (key.first) {
            builder.append(PARAM_GOOGLE_KEY).append(key.second)
        } else {
            throw IllegalArgumentException(key.second)
        }

        //Set location
        val location = locationCheck()
        if (location.first) {
            builder.append("&").append(PARAM_LOCATION).append(location.second)
        } else {
            throw IllegalArgumentException(location.second)
        }
        //Set keyword
        srkLocationBuilder.getKeyWord()?.let {
            builder.append("&").append(PARAM_KEYWORD).append(it)
        }
        //Set language
        languageCheck()?.let {
            if (it.first) {
                builder.append("&").append(PARAM_LANGUAGE).append(it.second)
            } else {
                throw IllegalArgumentException(it.second)
            }
        }
        //Set min price and max price
        maxPriceAndMinPriceCheck()?.let {
            if (it.first) {
                builder.append("&").append(PARAM_MINPRICE).append(it.second)
                builder.append("&").append(PARAM_MAXPRICE).append(it.third)
            } else {
                throw IllegalArgumentException(it.second)
            }
        }

        //Set open now
        srkLocationBuilder.getOpenNow()?.let {
            builder.append("&").append(PARAM_OPEN_NOW).append(it)
        }

        //Set page Token
        srkLocationBuilder.getPageToken()?.let {
            builder.append("&").append(PARAM_PAGE_TOKEN).append(it)
        }

        //Set radius or Rank By
        radiusAndRankByCheck().let {
            if (it.first) {
                builder.append("&").append(it.third).append(it.second)
            } else {
                throw IllegalArgumentException(it.second)
            }
        }

        placeTypeCheck()?.let {
            if (it.first) {
                builder.append("&").append(PARAM_TYPE).append(it.second)
            } else {
                throw IllegalArgumentException(it.second)
            }
        }
        return builder.toString()
    }

    fun getNearSearchPlaces() {
        val listener = srkLocationBuilder.getLocationResultListner()
        listener?.let { onLocationListener ->
            try {
                val finalConstructedURL = prepareNearByPlacesURL()
                onLocationListener.onLocationDetailsFetched(loading())
                val response = RetrofitServiceUtil().locationAPIService()
                    .getNearPlaces(NEAR_PLACES.plus(finalConstructedURL))
                response.enqueue(object : Callback<NearBySearchResponse?> {
                    override fun onResponse(
                        call: Call<NearBySearchResponse?>, response: Response<NearBySearchResponse?>
                    ) {
                        onLocationListener.onLocationDetailsFetched(try {
                            if (response.isSuccessful && response.body() != null) {
                                val nearBySearchResponse = response.body() as NearBySearchResponse
                                nearBySearchResponse.status?.let {
                                    if (it.equals(OK, true)) {
                                        if (srkLocationBuilder.getNeedResultInFormattedModel()) success(
                                            NEAR_PLACES,
                                            nearBySearchResponse.toFormattedNearByPlaceModelList()
                                        ) else success(
                                            NEAR_PLACES, nearBySearchResponse
                                        )

                                    } else {
                                        failure(NEAR_PLACES,
                                            nearBySearchResponse.error_message ?: run { it })
                                    }
                                } ?: run {
                                    failure(NEAR_PLACES, SOMETHING_WENT_WRONG)
                                }
                            } else if (response.errorBody() != null) {
                                error(NEAR_PLACES, response.errorBody())
                            } else {
                                failure(NEAR_PLACES, SOMETHING_WENT_WRONG)
                            }
                        } catch (e: Exception) {
                            exception(NEAR_PLACES, e.message.toString())
                        })
                    }

                    override fun onFailure(call: Call<NearBySearchResponse?>, e: Throwable) {
                        onLocationListener.onLocationDetailsFetched(
                            exception(NEAR_PLACES, e.message.toString())
                        )
                    }
                })
            } catch (e: Exception) {
                onLocationListener.onLocationDetailsFetched(
                    exception(NEAR_PLACES, e.message.toString())
                )
            }
        }

    }

    fun NearBySearchResponse.toFormattedNearByPlaceModelList(): List<FormattedNearByPlaceModel> {
        val placeModelList: ArrayList<FormattedNearByPlaceModel> = arrayListOf()
        this.results?.let {
            if (!this.results.isNullOrEmpty()) {
                for (item in it) {
                    if (item != null) {
                        val formattedNearByPlaceModel = FormattedNearByPlaceModel().apply {
                            placeId = item.placeId
                            locationName = item.name
                            fullAddress = item.vicinity
                            locationLat = item.geometry?.location?.lat
                            locationLong = item.geometry?.location?.lng
                        }
                        placeModelList.add(formattedNearByPlaceModel)
                    }
                }
            }
        }

        return placeModelList
    }

}