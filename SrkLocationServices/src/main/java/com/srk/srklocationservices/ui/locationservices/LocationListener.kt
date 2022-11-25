package com.srk.srklocationservices.ui.locationservices

import android.location.Location

interface LocationListener {
    fun locationOn()
    fun currentLocation(location: Location)
    fun locationCancelled()
    fun onFailed(locationEnum: LocationEnum)
}