package com.srk.srklocationservices.models.placedetails

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName

class PlaceDetailsModel {
    @SerializedName("html_attributions")
    @Expose
    private val htmlAttributions: List<Any>? = null

    @SerializedName("result")
    @Expose
    private val result: PlaceResult? = null

    @SerializedName("status")
    @Expose
    private val status: String? = null
}

