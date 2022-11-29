package com.srk.srklocationservices.utils

import android.content.Context
import android.location.Address
import android.location.Geocoder
import com.srk.srklocationservices.listners.OnLocationResultListner
import com.srk.srklocationservices.models.common.LocationNetworkStatus
import com.srk.srklocationservices.models.common.LocationResponse
import com.srk.srklocationservices.models.placedetails.FormattedPlaceDetailsModel
import java.io.IOException
import java.util.*

class SRKLocationUtilites {


    fun getAddress(context: Context, latitude: Double, longitude: Double): Address? {
        val geocoder = Geocoder(context, Locale.getDefault())
        try {
            val addresses: List<Address> = geocoder.getFromLocation(latitude, longitude, 1)
            if (addresses.size > 0) return addresses[0]
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return null
    }

    fun getCountryName(context: Context, latitude: Double, longitude: Double): String? {
        val address = getAddress(context, latitude, longitude)
        return if (address == null) "unknown" else address.countryName
    }

    fun getLocality(context: Context, latitude: Double, longitude: Double): String? {
        val address = getAddress(context, latitude, longitude)
        return if (address == null) "unknown" else address.locality
    }

    fun getStreet(context: Context, latitude: Double, longitude: Double): String? {
        val address = getAddress(context, latitude, longitude)
        return if (address == null) "unknown" else address.getAddressLine(0)
    }

    fun getAddressFromLatLng(
        context: Context,
        latitude: Double?,
        longitude: Double?,
        onLocationResultListner: OnLocationResultListner
    ) {
        try {
            val geocoder = Geocoder(context, Locale.getDefault())
            val addresses = geocoder.getFromLocation(
                latitude!!, longitude!!, 1
            )
            if (addresses != null && addresses.size > 0) {
                val address =
                    addresses[0].getAddressLine(0) // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                val city = addresses[0].locality
                val state = addresses[0].adminArea
                val country = addresses[0].countryName
                val postalCode = addresses[0].postalCode
                val knownName = addresses[0].featureName // Only if available else return NULL
                val formattedPlaceDetailsModel = FormattedPlaceDetailsModel()
                formattedPlaceDetailsModel.city = city
                formattedPlaceDetailsModel.fullAddress = address
                formattedPlaceDetailsModel.postal_code = postalCode
                formattedPlaceDetailsModel.country = country
                formattedPlaceDetailsModel.state = state

                onLocationResultListner.onLocationDetailsFetched(
                    LocationResponse(
                        response = formattedPlaceDetailsModel, LocationNetworkStatus.SUCCESS
                    )
                )
            }
        } catch (e: IOException) {
            e.printStackTrace()
            onLocationResultListner.onLocationDetailsFetched(
                LocationResponse(
                    message = e.message.toString(), networkStatus = LocationNetworkStatus.EXCEPTION
                )
            )
        }
    }
}