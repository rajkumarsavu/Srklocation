package com.srk.srklocationservices.models.autocomplete

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName

class AutoCompleteResponse {
    @SerializedName("predictions")
    @Expose
    val predictions: List<Predictions>? = null

    @SerializedName("error_message")
    @Expose
    var error_message: String? = null

    @SerializedName("status")
    @Expose
    val status: String? = null
}

