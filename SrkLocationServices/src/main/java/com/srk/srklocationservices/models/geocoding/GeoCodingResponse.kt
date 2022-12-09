package com.srk.srklocationservices.models.geocoding

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class GeoCodingResponse {
    @SerializedName("html_attributions")
    @Expose
    var htmlAttributions: List<Any?>? = null

    @SerializedName("next_page_token")
    @Expose
    var nextPageToken: String? = null

    @SerializedName("plus_code")
    @Expose
    var plus_code: PlusCode? = null

    @SerializedName("results")
    @Expose
    var results: List<GeoCodingResult?>? = null

    @SerializedName("error_message")
    @Expose
    var error_message: String? = null

    @SerializedName("status")
    @Expose
    var status: String? = null
}







