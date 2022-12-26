package com.srk.srklocationservices.ui.locationapis

import android.content.Context
import com.srk.srklocationservices.listners.OnLocationResultListner
import com.srk.srklocationservices.ui.locationapis.autocomplete.SRKGoogleAutoCompleteSearchAPI
import com.srk.srklocationservices.ui.locationapis.geocoding.SRKGoogleGeoCodingAPI
import com.srk.srklocationservices.ui.locationapis.nearbysearch.SRKGoogleNearPlacesAPI
import com.srk.srklocationservices.ui.locationapis.placedetails.SRKGooglePlaceDetailsAPI
import com.srk.srklocationservices.ui.locationapis.reversegeocoding.SRKGoogleReverseGeoCodingAPI

//FYI Note: Adding both `keyword` and `type` with the same value (`keyword=cafe&type=cafe` or `keyword=parking&type=parking`)
// can yield `ZERO_RESULTS`.
class SRKLocationBuilder {
    /**
     * Required API key,
     * https://developers.google.com/places/web-service/get-api-key
     */
    private var googleKey: String? = null
    fun getGoogleKey() = googleKey

    /**
     * REQUIRED
     * location — The latitude/longitude around which to retrieve place information.
     * This must be specified as latitude,longitude.
     * https://developers.google.com/maps/documentation/places/web-service/search-nearby#location
     */
    private var latitude = 0.0
    fun getLatitude() = latitude

    //Required
    private var longitude = 0.0
    fun getLongitude() = longitude


    /**
     * OPTIONAL
     * keyword — A term to be matched against all content that Google has indexed for this place,
     * including but not limited to name, type, and address,
     * as well as customer reviews and other third-party content.
     * https://developers.google.com/maps/documentation/places/web-service/search-nearby#keyword
     */
    private var keyword: String? = null
    fun getKeyWord() = keyword

    /**
     * OPTIONAL
     * the language code, indicating in which language the results should be returned,
     * https://developers.google.com/maps/documentation/places/web-service/search-nearby#language
     * https://developers.google.com/maps/documentation/places/web-service/details#language
     * https://developers.google.com/maps/documentation/places/web-service/autocomplete#language
     */
    private var language: String? = null
    fun getLanguage() = language


    /**
     * OPTIONAL
     * minprice and maxprice (optional) —
     * Restricts results to only those places within the specified range.
     * varid varues range between 0 (most affordable) to 4
     * https://developers.google.com/maps/documentation/places/web-service/search-nearby#maxprice
     */
    private var maxPrice: Int? = null
    fun getMaxPrice() = maxPrice

    /**
     * OPTIONAL
     * minprice and maxprice (optional) —
     * Restricts results to only those places within the specified range.
     * varid varues range between 0 (most affordable) to 4
     * https://developers.google.com/maps/documentation/places/web-service/search-nearby#minprice
     */
    private var minPrice: Int? = null
    fun getMinPrice() = minPrice

    /**
     * OPTIONAL
     * opennow — Returns only those places that are
     * open for business at the time the query is sent.
     * https://developers.google.com/maps/documentation/places/web-service/search-nearby#opennow
     */
    private var openNow: Boolean? = null
    fun getOpenNow() = openNow

    /**
     * OPTIONAL
     * radius — Defines the distance (in meters) within which to return place results.
     * The maximum allowed radius is 50 000 meters.
     * Note that radius must not be included if rankby=distance
     * (described under Optional parameters below) is specified.
     * When using rankby=distance, the radius parameter will not be accepted, and will result in an INVALID_REQUEST.
     * https://developers.google.com/maps/documentation/places/web-service/search-nearby#radius
     */
    private var radius: Int? = null
    fun getRadius() = radius

    /**
     * OPTIONAL
     * prominence & distance
     * rankby=distance (described under Optional parameters below) is specified,
     * then one or more of keyword, name, or type is required.
     * https://developers.google.com/maps/documentation/places/web-service/search-nearby#rankby
     */
    private var rankby: String? = null
    fun getRankBy() = rankby

    /**
     * OPTIONAL
     * https://developers.google.com/maps/documentation/places/web-service/search-nearby#pagetoken
     */
    private var pageToken: Int? = null
    fun getPageToken() = pageToken

    /**
     * OPTIONAL
     * type — Restricts the results to places matching the specified type.
     * Only one type may be specified (if more than one type is provided,
     * all types following the first entry are ignored).
     * https://developers.google.com/maps/documentation/places/web-service/search-nearby#type
     */
    private var type: String? = null
    fun getType() = type

    private var needResultInFormattedModel: Boolean = false
    fun getNeedResultInFormattedModel() = needResultInFormattedModel

    //=====================Place Details========

    /**
     * Required
     * A textual identifier that uniquely identifies a place, returned from a Place Search
     * https://developers.google.com/maps/documentation/places/web-service/details#place_id
     */
    private var placeId: String? = null
    fun getPlaceId() = placeId

    /**
     * OPTIONAL
     * Use the fields parameter to specify a comma-separated list of place data types to return
     *  For example: fields=formatted_address,name,geometry.
     *  Use a forward slash when specifying compound values. For example: opening_hours/open_now.
     *  https://developers.google.com/maps/documentation/places/web-service/details#fields
     */
    private var fields: String? = null
    fun getFields() = fields

    /**
     * OPTIONAL
     * The region code, specified as a ccTLD ("top-level domain") two-character value.
     * https://developers.google.com/maps/documentation/places/web-service/details#region
     */
    private var region: String? = null
    fun getRegion() = region

    /**
     * OPTIONAL
     * Specify reviews_no_translations=true to disable translation of reviews;
     * specify reviews_no_translations=false to enable translation of reviews.
     * Reviews are returned in their original language.
     * https://developers.google.com/maps/documentation/places/web-service/search-nearby#pagetoken
     */
    private var reviewsNoTranslations: Boolean? = null
    fun getReviewsNoTranslations() = reviewsNoTranslations

    /**
     * OPTIONAL
     * The sorting method to use when returning reviews. Can be set to most_relevant (default) or newest.
     * most_relevant(default), reviews are sorted by relevance; the service will bias the results to return reviews originally written in the preferred language.
     * newest reviews are sorted in chronological order; the preferred language does not affect the sort order.
     * https://developers.google.com/maps/documentation/places/web-service/details#reviews_sort
     */
    private var reviewsSort: String? = null
    fun getReviewsSort() = reviewsSort

    /**
     * OPTIONAL
     * A random string which identifies an autocomplete session for billing purposes.
     * https://developers.google.com/maps/documentation/places/web-service/details#sessiontoken
     */
    private var sessiontoken: String? = null
    fun getSessiontoken() = sessiontoken

    //=====================Autcomplete search========
    /**
     * REQUIRED
     * A random string which identifies an autocomplete session for billing purposes.
     * https://developers.google.com/maps/documentation/places/web-service/autocomplete#input
     */
    private var input: String? = null
    fun getInput() = input

    /**
     * OPTIONAL
     * A grouping of places to which you would like to restrict your results.
     * https://developers.google.com/maps/documentation/places/web-service/autocomplete#components
     */
    private var components: String? = null
    fun getComponents() = components

    /**
     * OPTIONAL
     * The position, in the input term, of the last character that the service uses to match predictions.
     * For example, if the input is Google and the offset is 3, the service will match on Goo.
     * https://developers.google.com/maps/documentation/places/web-service/autocomplete#offset
     */
    private var offset: String? = null
    fun getOffset() = offset

    /**
     * OPTIONAL
     * The origin point from which to calculate straight-line distance to the destination (returned as distance_meters).
     * If this value is omitted, straight-line distance will not be returned. Must be specified as latitude,longitude.
     * https://developers.google.com/maps/documentation/places/web-service/autocomplete#origin
     */
    private var origin: String? = null
    fun getOrigin() = origin

    /**
     * OPTIONAL
     * Returns only those places that are strictly within the region defined by location and radius.
     * This is a restriction, rather than a bias, meaning that results outside this region will not be returned even if they match the user input.
     * https://developers.google.com/maps/documentation/places/web-service/autocomplete#strictbounds
     */
    private var strictbounds: Boolean? = null
    fun getStrictBounds() = strictbounds

    //============= Google Geo coding===================
    /**
     * REQUIRED
     * */
    private var address: String? = null
    fun getAddress() = address


    private var onLocationResultListner: OnLocationResultListner? = null
    fun getLocationResultListner() = onLocationResultListner

    //============= Near by search API===================
    fun googleApiKey(key: String?): SRKLocationBuilder {
        googleKey = key
        return this
    }

    fun locationLatLng(lat: Double, lng: Double): SRKLocationBuilder {
        latitude = lat
        longitude = lng
        return this
    }

    fun keyword(keywordVal: String?): SRKLocationBuilder {
        keyword = keywordVal
        return this
    }

    fun language(languageVal: String?): SRKLocationBuilder {
        language = languageVal
        return this
    }

    fun maxPrice(maxPriceVal: Int): SRKLocationBuilder {
        maxPrice = maxPriceVal
        return this
    }

    fun minPrice(minPriceVal: Int): SRKLocationBuilder {
        minPrice = minPriceVal
        return this
    }

    fun openNow(openNowVal: Boolean): SRKLocationBuilder {
        openNow = openNowVal
        return this
    }

    fun pageToken(pageTokenValue: Int): SRKLocationBuilder {
        pageToken = pageTokenValue
        return this
    }

    fun radius(radiusValue: Int): SRKLocationBuilder {
        radius = radiusValue
        return this
    }

    fun rankBy(rankbyValue: String): SRKLocationBuilder {
        rankby = rankbyValue
        return this
    }

    fun type(typeValue: String): SRKLocationBuilder {
        type = typeValue
        return this
    }

    fun needResultInFormattedModel(): SRKLocationBuilder {
        needResultInFormattedModel = true
        return this
    }

    //============= Place details API===================
    fun placeId(placeIdVal: String): SRKLocationBuilder {
        placeId = placeIdVal
        return this
    }

    fun fields(fieldsVal: String?): SRKLocationBuilder {
        fields = fieldsVal
        return this
    }

    fun region(regionVal: String?): SRKLocationBuilder {
        region = regionVal
        return this
    }

    fun reviewsNoTranslations(reviewsNoTranslationsVal: Boolean): SRKLocationBuilder {
        reviewsNoTranslations = reviewsNoTranslationsVal
        return this
    }

    fun reviewsSort(reviewsSortValue: String?): SRKLocationBuilder {
        reviewsSort = reviewsSortValue
        return this
    }

    fun sessiontoken(sessiontokenValue: String?): SRKLocationBuilder {
        sessiontoken = sessiontokenValue
        return this
    }

    fun needPlaceDetailsResultInPlaceModel(fomatedList: Boolean): SRKLocationBuilder {
        needResultInFormattedModel = fomatedList
        return this
    }

    //=====================Auto complete search==========
    fun input(inputVal: String): SRKLocationBuilder {
        input = inputVal
        return this
    }

    fun components(componentsVal: String?): SRKLocationBuilder {
        components = componentsVal
        return this
    }

    fun offset(offsetVal: String?): SRKLocationBuilder {
        offset = offsetVal
        return this
    }

    fun origin(originVal: String?): SRKLocationBuilder {
        origin = originVal
        return this
    }

    fun strictbounds(strictboundsVal: Boolean): SRKLocationBuilder {
        strictbounds = strictboundsVal
        return this
    }

    //============= Google Geo coding===================

    fun address(addressVal: String?): SRKLocationBuilder {
        address = addressVal
        return this
    }

    fun onLocationResultListener(onLocationResultListenerValue: OnLocationResultListner): SRKLocationBuilder {
        onLocationResultListner = onLocationResultListenerValue
        return this
    }


    fun buildNearPlaces(): SRKGoogleNearPlacesAPI {
        return SRKGoogleNearPlacesAPI(this)
    }

    fun buildPlaceDetails(): SRKGooglePlaceDetailsAPI {
        return SRKGooglePlaceDetailsAPI(this)
    }

    fun buildAutoCompleteSearch(): SRKGoogleAutoCompleteSearchAPI {
        return SRKGoogleAutoCompleteSearchAPI(this)
    }

    fun buildGeoCoding(): SRKGoogleGeoCodingAPI {
        return SRKGoogleGeoCodingAPI(this)
    }

    fun buildReverseGeoCoding(): SRKGoogleReverseGeoCodingAPI {
        return SRKGoogleReverseGeoCodingAPI(this)
    }

}