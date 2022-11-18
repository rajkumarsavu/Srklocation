package com.srk.srklocationservices.models.common

import com.google.gson.annotations.SerializedName

open class CommonResponse(
    @SerializedName("message") var message: String? = null,
    @SerializedName("statusCode") var statusCode: Int? = null,
    @SerializedName("status") var status: String? = null,
    @SerializedName("error") var error: String = "",
    @SerializedName("timestamp") var timestamp: Long? = null
)
