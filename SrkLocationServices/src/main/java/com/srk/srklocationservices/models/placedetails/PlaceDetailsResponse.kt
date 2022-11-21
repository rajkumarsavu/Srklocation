package com.srk.srklocationservices.models.placedetails

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName

class PlaceDetailsResponse {
    @SerializedName("html_attributions")
    @Expose
    val htmlAttributions: List<Any>? = null

    @SerializedName("result")
    @Expose
    val result: PlaceResult? = null

    @SerializedName("error_message")
    @Expose
    var error_message: String? = null

    @SerializedName("status")
    @Expose
    val status: String? = null
}

