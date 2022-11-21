package com.srk.srklocationservices.listners

import com.srk.srklocationservices.models.common.LocationResponse

interface OnLocationResultListner {
    fun onLocationDetailsFetched(locationResponse: LocationResponse)
}