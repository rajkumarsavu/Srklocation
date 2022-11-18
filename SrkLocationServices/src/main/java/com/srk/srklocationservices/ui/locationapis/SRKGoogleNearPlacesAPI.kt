package com.srk.srklocationservices.ui.locationapis

import com.app.joulez.networklibrary.models.response.location.NearBySearchResponse
import com.srk.srklocationservices.api.RetrofitServiceUtil
import com.srk.srklocationservices.utils.AppConstants.NEAR_PLACES
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
            throw IllegalArgumentException(key.second);
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
                throw IllegalArgumentException(location.second)
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

        //Set opennow
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
        listener?.let { onLocationListner ->
            try {
                val finalConstructedURL = prepareNearByPlacesURL()
                onLocationListner.onPlaceDetailsFetched(loading())
                val response = RetrofitServiceUtil().locationAPIService()
                    .getNearPlaces(NEAR_PLACES.plus(finalConstructedURL))
                response.enqueue(object : Callback<NearBySearchResponse?> {
                    override fun onResponse(
                        call: Call<NearBySearchResponse?>, response: Response<NearBySearchResponse?>
                    ) {
                        onLocationListner.onPlaceDetailsFetched(try {
                            if (response.isSuccessful && response.body() != null) {
                                val nearBySearchResponse = response.body() as NearBySearchResponse
                                nearBySearchResponse.status?.let {
                                    if (it.equals("OK", true)) {
                                        success(NEAR_PLACES, nearBySearchResponse)
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
                        onLocationListner.onPlaceDetailsFetched(
                            exception(NEAR_PLACES, e.message.toString())
                        )
                    }
                })
            } catch (e: Exception) {
                onLocationListner.onPlaceDetailsFetched(
                    exception(NEAR_PLACES, e.message.toString())
                )
            }
        }

    }

}