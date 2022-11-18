package com.srk.srklocationservices.ui.locationapis

import com.srk.srklocationservices.listners.OnLocationResultListner

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
    private var latitude = -1.0
    fun getLatitude() = latitude

    //Required
    private var longitude = -1.0
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

    private var onLocationResultListner: OnLocationResultListner? = null
    fun getLocationResultListner() = onLocationResultListner

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

    fun onLocationResultListner(onLocationResultListnerValue: OnLocationResultListner): SRKLocationBuilder {
        onLocationResultListner = onLocationResultListnerValue
        return this
    }

    fun buildNearPlaces(): SRKGoogleNearPlacesAPI {
        return SRKGoogleNearPlacesAPI(this)
    }


}