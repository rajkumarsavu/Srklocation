package com.srk.srklocationservices.ui.locationapis.autocomplete

import com.srk.srklocationservices.api.RetrofitServiceUtil
import com.srk.srklocationservices.models.autocomplete.AutoCompleteResponse
import com.srk.srklocationservices.models.autocomplete.FormattedAutoCompleteModel
import com.srk.srklocationservices.ui.locationapis.GooglePlacesAPI
import com.srk.srklocationservices.ui.locationapis.SRKLocationBuilder
import com.srk.srklocationservices.utils.AppConstants
import com.srk.srklocationservices.utils.AppConstants.OK
import com.srk.srklocationservices.utils.AppConstants.SEARCH_PLACES
import com.srk.srklocationservices.utils.AppConstants.SOMETHING_WENT_WRONG
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SRKGoogleAutoCompleteSearchAPI(srkLocationBuilder: SRKLocationBuilder) :
    GooglePlacesAPI(srkLocationBuilder) {


    private fun prepareAutoCompleteSearchURL(): String {
        val builder: StringBuilder = StringBuilder()
        //Set google API Key
        val key = checkGoogleApiKey()
        if (key.first) {
            builder.append(PARAM_GOOGLE_KEY).append(key.second)
        } else {
            throw IllegalArgumentException(key.second)
        }
        //Set input
        val input = checkInput()
        if (input.first) {
            builder.append("&").append(PARAM_INPUT).append(input.second)
        } else {
            throw IllegalArgumentException(input.second)
        }

        //Set location
        val location = locationCheck()
        if (location.first) {
            builder.append("&").append(PARAM_LOCATION).append(location.second)
        } else {
            throw IllegalArgumentException(location.second)
        }

        //Set offset
        srkLocationBuilder.getOffset()?.let {
            builder.append("&").append(PARAM_OFF_SET).append(it)
        }

        //Set Origin
        srkLocationBuilder.getOrigin()?.let {
            builder.append("&").append(PARAM_ORIGIN).append(it)
        }

        //Set Origin
        srkLocationBuilder.getRegion()?.let {
            builder.append("&").append(PARAM_REGION).append(it)
        }

        //Set Origin
        srkLocationBuilder.getSessiontoken()?.let {
            builder.append("&").append(PARAM_SESSIONTOKEN).append(it)
        }

        //Set Origin
        srkLocationBuilder.getStrictBounds()?.let {
            builder.append("&").append(PARAM_STRICTBOUNDS).append(it)
        }

        checkStrictBonds().let {
            if (it.first.first) {
                if (it.first.second) {
                    builder.append("&").append(PARAM_LOCATION).append(it.second)
                    builder.append("&").append(PARAM_RADIUS).append(it.third)
                    builder.append("&").append(PARAM_STRICTBOUNDS).append(true)
                } else {
                    throw IllegalArgumentException(it.second)
                }
            } else {
                val locationCheck = locationCheck()
                if (location.first) {
                    builder.append("&").append(PARAM_LOCATION).append(locationCheck.second)
                } else {
                    //Nothing will append
                }

                val radius = srkLocationBuilder.getRadius()
                if (radius != null) {
                    if (radius > 50000) {
                        throw IllegalArgumentException(AppConstants.RADIUS_CANT_BE_GRATER)
                    } else if (radius < 1) {
                        throw IllegalArgumentException(AppConstants.RADIUS_CANT_BE_LESSER)
                    } else {
                        builder.append("&").append(PARAM_RADIUS).append(radius)
                    }
                } else {
                    //Nothing will append
                }

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

    fun getAutoCompleteSearch() {
        val listener = srkLocationBuilder.getLocationResultListner()
        listener?.let { onLocationListener ->
            try {
                val finalConstructedURL = prepareAutoCompleteSearchURL()
                onLocationListener.onLocationDetailsFetched(loading())
                val response = RetrofitServiceUtil().locationAPIService()
                    .getAutoCompletePlaces(SEARCH_PLACES.plus(finalConstructedURL))
                response.enqueue(object : Callback<AutoCompleteResponse?> {
                    override fun onResponse(
                        call: Call<AutoCompleteResponse?>, response: Response<AutoCompleteResponse?>
                    ) {
                        onLocationListener.onLocationDetailsFetched(try {
                            if (response.isSuccessful && response.body() != null) {
                                val autoCompleteResponse = response.body() as AutoCompleteResponse
                                autoCompleteResponse.status?.let {
                                    if (it.equals(OK, true)) {
                                        if (srkLocationBuilder.getNeedResultInFormattedModel()) success(
                                            SEARCH_PLACES,
                                            autoCompleteResponse.toFormattedAutoCompleteList()
                                        ) else success(
                                            SEARCH_PLACES, autoCompleteResponse
                                        )

                                    } else {
                                        failure(SEARCH_PLACES,
                                            autoCompleteResponse.error_message ?: run { it })
                                    }
                                } ?: run {
                                    failure(SEARCH_PLACES, SOMETHING_WENT_WRONG)
                                }
                            } else if (response.errorBody() != null) {
                                error(SEARCH_PLACES, response.errorBody())
                            } else {
                                failure(SEARCH_PLACES, SOMETHING_WENT_WRONG)
                            }
                        } catch (e: Exception) {
                            exception(SEARCH_PLACES, e.message.toString())
                        })
                    }

                    override fun onFailure(call: Call<AutoCompleteResponse?>, e: Throwable) {
                        onLocationListener.onLocationDetailsFetched(
                            exception(SEARCH_PLACES, e.message.toString())
                        )
                    }
                })
            } catch (e: Exception) {
                onLocationListener.onLocationDetailsFetched(
                    exception(SEARCH_PLACES, e.message.toString())
                )
            }
        }

    }

    fun AutoCompleteResponse.toFormattedAutoCompleteList(): List<FormattedAutoCompleteModel> {
        val autoCompleteModelList: ArrayList<FormattedAutoCompleteModel> = arrayListOf()
        this.predictions?.let {
            if (!this.predictions.isEmpty()) {
                for (item in it) {
                    val formattedAutoCompleteModel = FormattedAutoCompleteModel().apply {
                        placeId = item.placeId
                        locationName = item.description
                        fullAddress = item.description
                        mainText = item.structuredFormatting?.mainText
                        secondaryText = item.structuredFormatting?.secondaryText
                    }
                    autoCompleteModelList.add(formattedAutoCompleteModel)
                }
            }
        }

        return autoCompleteModelList
    }

}