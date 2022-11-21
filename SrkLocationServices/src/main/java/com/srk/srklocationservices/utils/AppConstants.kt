package com.srk.srklocationservices.utils

object AppConstants {
    //Google location APIS

    const val GOOGLE_SERVICE_API = "https://maps.googleapis.com/maps/"
    const val NEAR_PLACES = "api/place/nearbysearch/json?"
    const val SEARCH_PLACES = "api/place/autocomplete/json?"
    const val PLACE_DETAILS = "api/place/details/json?"

    //Messages
    const val GOOGLE_KEY_NOT_FOUND = "Google API key is required"
    const val LATITUDE_REQUIRED = "Latitude is required"
    const val LONGITUDE_REQUIRED = "Longitude is required"
    const val INVALID_LANGUAGE = "Invalid language code"
    const val PRICE_GRATER_THAN_ZERO = "Min Price' and 'Max Price' should be greater equal to 0"
    const val PRICE_LESS_THAN_FOUR = "Min Price' and 'Max Price' should be less equal to 4"
    const val PRICE_MIN_MAX = "Min Price should be less than Max Price"
    const val RADIUS_AND_RANK_BY_CONT_WORK = "Radius and Rank By cannot work together"
    const val RADIUS_AND_RANK_REQIRED = "Radius or Rank By either any one required"
    const val RADIUS_REQUIRED = "Radius is required"
    const val RADIUS_CANT_BE_GRATER = "Radius  cannot be grater than 50000"
    const val RADIUS_CANT_BE_LESSER = "Radius  cannot be less than 1"
    const val RANK_CAN_ONLY = "Rank By can only be 'distance' or 'prominence'"

    const val PLACE_ID_REQUIRED = "Place Id required"


    const val SOMETHING_WENT_WRONG = "Something went wrong"
    const val OK = "OK"

}