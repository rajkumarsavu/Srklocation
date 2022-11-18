package com.srk.srklocationservices.models.common

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class OpeningHours {
    @SerializedName("open_now")
    @Expose
    var openNow: Boolean? = null

    @SerializedName("weekday_text")
    @Expose
    var weekdayText: List<Any>? = null
}
