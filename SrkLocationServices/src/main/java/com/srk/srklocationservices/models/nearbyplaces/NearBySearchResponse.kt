package com.app.joulez.networklibrary.models.response.location

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class NearBySearchResponse {
    @SerializedName("html_attributions")
    @Expose
    var htmlAttributions: List<Any?>? = null

    @SerializedName("next_page_token")
    @Expose
    var nextPageToken: String? = null

    @SerializedName("results")
    @Expose
    var results: List<NearByPlaceResult?>? = null

    @SerializedName("error_message")
    @Expose
    var error_message: String? = null

    @SerializedName("status")
    @Expose
    var status: String? = null
}







