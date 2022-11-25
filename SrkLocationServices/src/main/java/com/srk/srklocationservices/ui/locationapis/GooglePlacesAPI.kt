package com.srk.srklocationservices.ui.locationapis

import android.text.TextUtils
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.srk.srklocationservices.models.common.CommonResponse
import com.srk.srklocationservices.models.common.LocationNetworkStatus
import com.srk.srklocationservices.models.common.LocationResponse
import com.srk.srklocationservices.utils.AppConstants.GOOGLE_KEY_NOT_FOUND
import com.srk.srklocationservices.utils.AppConstants.INPUT_REQUIRED
import com.srk.srklocationservices.utils.AppConstants.INVALID_LANGUAGE
import com.srk.srklocationservices.utils.AppConstants.LATITUDE_REQUIRED
import com.srk.srklocationservices.utils.AppConstants.LONGITUDE_REQUIRED
import com.srk.srklocationservices.utils.AppConstants.PLACE_ID_REQUIRED
import com.srk.srklocationservices.utils.AppConstants.PRICE_GRATER_THAN_ZERO
import com.srk.srklocationservices.utils.AppConstants.PRICE_LESS_THAN_FOUR
import com.srk.srklocationservices.utils.AppConstants.PRICE_MIN_MAX
import com.srk.srklocationservices.utils.AppConstants.RADIUS_AND_LAT_LANG_REQUIRED_BOTH
import com.srk.srklocationservices.utils.AppConstants.RADIUS_AND_RANK_BY_CONT_WORK
import com.srk.srklocationservices.utils.AppConstants.RADIUS_AND_RANK_REQIRED
import com.srk.srklocationservices.utils.AppConstants.RADIUS_CANT_BE_GRATER
import com.srk.srklocationservices.utils.AppConstants.RADIUS_CANT_BE_LESSER
import com.srk.srklocationservices.utils.AppConstants.RADIUS_REQUIRED
import com.srk.srklocationservices.utils.AppConstants.RANK_CAN_ONLY
import com.srk.srklocationservices.utils.AppConstants.SOMETHING_WENT_WRONG
import okhttp3.ResponseBody
import java.util.*

open class GooglePlacesAPI(val srkLocationBuilder: SRKLocationBuilder) {
    protected var PARAM_GOOGLE_KEY = "key="

    //======Near By Search API======
    protected var PARAM_LOCATION = "location="
    protected var PARAM_LANGUAGE = "language="
    protected var PARAM_MINPRICE = "minprice="
    protected var PARAM_MAXPRICE = "maxprice="
    protected var PARAM_OPEN_NOW = "opennow="
    protected var PARAM_PAGE_TOKEN = "pagetoken="
    protected var PARAM_RADIUS = "radius="
    protected var PARAM_RANK_BY = "rankby="
    protected var PARAM_KEYWORD = "keyword="
    protected var PARAM_TYPE = "type="

    //======Place Details API======
    protected var PARAM_PLACE_ID = "place_id="
    protected var PARAM_FIELDS = "fields="
    protected var PARAM_REGION = "region="
    protected var PARAM_REVIEWS_NO_TRANSLATIONS = "reviews_no_translations ="
    protected var PARAM_REVIEWS_SORT = "reviews_sort="
    protected var PARAM_SESSIONTOKEN = "sessiontoken="

    //======Auto complete search======
    protected var PARAM_INPUT = "input="
    protected var PARAM_OFF_SET = "offset="
    protected var PARAM_ORIGIN = "origin="
    protected var PARAM_STRICTBOUNDS = "strictbounds="

    fun loading() = LocationResponse(
        networkStatus = LocationNetworkStatus.LOADING
    )

    fun success(requestType: String, response: Any?) = LocationResponse(
        networkStatus = LocationNetworkStatus.SUCCESS,
        requestType = requestType,
        response = response,
        message = "success"
    )


    fun failure(requestType: String, message: String) = LocationResponse(
        networkStatus = LocationNetworkStatus.ERROR_OR_FAIL,
        requestType = requestType,
        message = message

    )

    fun exception(requestType: String, message: String) = LocationResponse(
        networkStatus = LocationNetworkStatus.EXCEPTION,
        requestType = requestType,
        message = message
    )

    fun error(requestType: String, response: ResponseBody?): LocationResponse {
        return if (response != null) {
            val gson = Gson()
            val type = object : TypeToken<CommonResponse>() {}.type
            val errorResponse: CommonResponse? = gson.fromJson(response.charStream(), type)
            LocationResponse(
                networkStatus = LocationNetworkStatus.ERROR_OR_FAIL,
                requestType = requestType,
                response = errorResponse
            )
        } else {
            failure(requestType, SOMETHING_WENT_WRONG)
        }
    }

    fun checkGoogleApiKey(): Pair<Boolean, String> {

        if (!TextUtils.isEmpty(srkLocationBuilder.getGoogleKey())) {
            return Pair(true, srkLocationBuilder.getGoogleKey().orEmpty())
        }
        return Pair(false, GOOGLE_KEY_NOT_FOUND)
    }

    fun locationCheck(): Pair<Boolean, String> {
        if (srkLocationBuilder.getLatitude() <= 0) {
            return Pair(false, LATITUDE_REQUIRED)
        } else if (srkLocationBuilder.getLongitude() <= 0) {
            return Pair(false, LONGITUDE_REQUIRED)
        } else return Pair(
            true,
            srkLocationBuilder.getLatitude().toString().plus(",")
                .plus(srkLocationBuilder.getLongitude())
        )
    }

    fun languageCheck(): Pair<Boolean, String>? {
        val language = srkLocationBuilder.getLanguage()
        if (!TextUtils.isEmpty(language)) {
            var locale = Locale(language.orEmpty())
            if (locale.isO3Country.isEmpty()) {
                locale = Locale(language.orEmpty(), language.orEmpty())
                if (locale.isO3Country.isEmpty()) {
                    return Pair(false, INVALID_LANGUAGE)
                } else {
                    return Pair(true, language.orEmpty())
                }
            }
            return Pair(false, INVALID_LANGUAGE)
        }
        return null
    }


    fun maxPriceAndMinPriceCheck(): Triple<Boolean, String, String>? {
        val minPrice = srkLocationBuilder.getMinPrice()
        val maxPrice = srkLocationBuilder.getMaxPrice()

        if (minPrice == null || maxPrice == null) {
            return null
        } else if (minPrice < 0 || maxPrice < 0) {
            return Triple(false, PRICE_GRATER_THAN_ZERO, "")
        } else if (minPrice > 4 || maxPrice > 4) {
            return Triple(false, PRICE_LESS_THAN_FOUR, "")
        } else if (minPrice > maxPrice) {
            return Triple(false, PRICE_MIN_MAX, "")
        } else {
            return Triple(true, minPrice.toString(), maxPrice.toString())
        }
    }

    fun radiusAndRankByCheck(): Triple<Boolean, String, String> {
        val radius = srkLocationBuilder.getRadius()
        val rankby = srkLocationBuilder.getRankBy()
        if (radius != null || !TextUtils.isEmpty(rankby)) {
            if (radius != null && rankby != null) {
                return Triple(false, RADIUS_AND_RANK_BY_CONT_WORK, "")
            } else if (radius == null && rankby == null) {
                return Triple(false, RADIUS_REQUIRED, "")
            } else if (radius != null) {
                if (radius > 50000) {
                    return Triple(false, RADIUS_CANT_BE_GRATER, "")
                } else if (radius < 1) {
                    return Triple(false, RADIUS_CANT_BE_LESSER, "")
                }
                return Triple(true, radius.toString(), PARAM_RADIUS)
            } else {
                if (rankby.equals("distance") || rankby.equals("prominence")) {
                    return Triple(true, rankby.toString(), PARAM_RANK_BY)
                } else {
                    return Triple(false, RANK_CAN_ONLY, "")
                }
            }
        } else {
            return Triple(false, RADIUS_AND_RANK_REQIRED, "")
        }
    }

    fun placeTypeCheck(): Pair<Boolean, String>? {

        val type = srkLocationBuilder.getType()
        if (!TextUtils.isEmpty(type)) {
            val someClass: Class<*> = PlaceType::class.java
            try {
                someClass.getField(type.toString().uppercase(Locale.getDefault()))
                return Pair(true, type.orEmpty())
            } catch (e: java.lang.Exception) {
                return Pair(
                    false,
                    "'" + type.toString() + "' is invalid. All types are given in PlaceType class"
                )
            }
        } else {
            return null
        }
    }

    fun checkPlaceId(): Pair<Boolean, String> {

        if (!TextUtils.isEmpty(srkLocationBuilder.getPlaceId())) {
            return Pair(true, srkLocationBuilder.getPlaceId().orEmpty())
        }
        return Pair(false, PLACE_ID_REQUIRED)
    }

    fun checkInput(): Pair<Boolean, String> {

        if (!TextUtils.isEmpty(srkLocationBuilder.getInput())) {
            return Pair(true, srkLocationBuilder.getInput().orEmpty())
        }
        return Pair(false, INPUT_REQUIRED)
    }

    fun checkStrictBonds(): Triple<Pair<Boolean, Boolean>, String, String> {
        val radius = srkLocationBuilder.getRadius()
        val location = locationCheck()

        if (srkLocationBuilder.getStrictBounds() == true) {
            if (location.first && radius != null) {
                if (radius > 50000) {
                    return Triple(Pair(true, false), RADIUS_CANT_BE_GRATER, "")
                } else if (radius < 1) {
                    return Triple(Pair(true, false), RADIUS_CANT_BE_LESSER, "")
                } else {
                    return Triple(Pair(true, true), location.second, radius.toString())
                }
            } else {
                return Triple(Pair(true, false), RADIUS_AND_LAT_LANG_REQUIRED_BOTH, "")
            }
        } else {
            return Triple(Pair(false, false), RADIUS_AND_LAT_LANG_REQUIRED_BOTH, "")
        }
    }
}

