package com.srk.srklocationservices.models.common

import androidx.annotation.Keep

@Keep
data class LocationResponse(
    val response: Any? = null,
    val networkStatus: LocationNetworkStatus,
    val message: String = "",
    val requestType: String = ""
)
