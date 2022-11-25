package com.srk.srklocationservices.ui.locationservices.helper

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Looper
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.srk.srklocationservices.ui.locationservices.LocationEnum
import com.srk.srklocationservices.ui.locationservices.SRKLocationUtilService
import java.lang.ref.WeakReference

class SRKLocationSettingAndRequestHelper(
    val builder: SRKLocationUtilService.Builder,
    private val listener: Listener
) {
    private var locationCallback: LocationCallback? = null
    private val activityWeakReference = WeakReference(builder.getContext())

    fun checkLocationSettingsIfNotShow() {
        val ref = activityWeakReference.get() ?: return
        //Create builder
        val reqBuilder = LocationSettingsRequest.Builder().addLocationRequest(
            getLocationRequest(builder)
        ).build()
        //settings client reference
        val task = LocationServices.getSettingsClient(ref).checkLocationSettings(reqBuilder)
        task.addOnSuccessListener {
            if (activityWeakReference.get() == null) return@addOnSuccessListener
            listener.onSuccess()
        }.addOnFailureListener { exception ->
            if (exception is ResolvableApiException) {
                val referenceNew = activityWeakReference.get() ?: return@addOnFailureListener
                // Location settings are not satisfied, but this can be fixed by showing the user a dialog.
                try {
                    // Show the dialog by calling startResolutionForResult(),
                    // and check the result in onActivityResult().
                    exception.startResolutionForResult(referenceNew as Activity, REQUEST_CODE)
                } catch (sendEx: Exception) {
                    listener.onFailed(LocationEnum.COULD_NOT_OPTIMIZE_DEVICE_HARDWARE)
                }
            } else {
                if (activityWeakReference.get() == null) return@addOnFailureListener
                listener.onFailed(LocationEnum.COULD_NOT_OPTIMIZE_DEVICE_HARDWARE)
            }
        }
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val activityTemp = activityWeakReference.get() ?: return
        if (requestCode == REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                listener.onSuccess()
            } else {
                val locationManager =
                    activityTemp.getSystemService(Context.LOCATION_SERVICE) as LocationManager
                if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    if (activityWeakReference.get() == null) return

                    listener.onFailed(LocationEnum.HIGH_PRECISION_LOCATION_NA_TRY_AGAIN_PREFERABLY_WITH_NETWORK_CONNECTIVITY)
                } else {
                    if (activityWeakReference.get() == null) return
                    listener.onFailed(LocationEnum.LOCATION_OPTIMIZATION_PERMISSION_NOT_GRANTED)
                }
            }
        }
    }


    fun addLifecycleListener(fusedLocationClient: FusedLocationProviderClient) {
        val ref = activityWeakReference.get() ?: return
        (ref as LifecycleOwner).lifecycle.addObserver(object : LifecycleObserver {
            @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
            fun connectListener() {
                if (activityWeakReference.get() == null) return
                startLocationUpdates(fusedLocationClient)
            }

            @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
            fun disconnectListener() {
                if (activityWeakReference.get() == null) return
                stopLocationUpdates(fusedLocationClient)
            }
        })
    }

    fun startLocationUpdates(fusedLocationClient: FusedLocationProviderClient) {
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                if (activityWeakReference.get() == null) return
                for (location in locationResult.locations) {
                    builder.getLocationListener().currentLocation(location)
                }
                if (!builder.getLocationFrequently()) {
                    stopLocationUpdates(fusedLocationClient)
                }
            }

            override fun onLocationAvailability(locationAvailability: LocationAvailability) {
                if (activityWeakReference.get() == null) return
                if (!locationAvailability.isLocationAvailable) {
                    builder.getLocationListener()
                        .onFailed(LocationEnum.HIGH_PRECISION_LOCATION_NA_TRY_AGAIN_PREFERABLY_WITH_NETWORK_CONNECTIVITY)
                    locationCallback?.let { fusedLocationClient.removeLocationUpdates(it) }
                }
            }
        }.apply {
            if (ActivityCompat.checkSelfPermission(
                    builder.getContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    builder.getContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
            fusedLocationClient.requestLocationUpdates(
                getLocationRequest(builder), this, Looper.myLooper()
            )
        }
    }

    fun stopLocationUpdates(fusedLocationClient: FusedLocationProviderClient) {
        locationCallback?.let {
            fusedLocationClient.removeLocationUpdates(it)
        }
    }


    companion object {
        const val REQUEST_CODE = 1235

        interface Listener {
            fun onSuccess()
            fun onFailed(locationEnum: LocationEnum)
        }

        fun getLocationRequest(
            builder: SRKLocationUtilService.Builder
        ): LocationRequest {

            var locationRequest = builder.getLocationRequest()

            if (locationRequest == null) {
                val locBuilder = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 1000)
                locBuilder.setWaitForAccurateLocation(false)
                if (!builder.getLocationFrequently()) {
                    locBuilder.setMaxUpdates(1)
                }
                locationRequest = locBuilder.build()
            }
            return locationRequest
            /*LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 1000)
                .setWaitForAccurateLocation(false)
                .setMaxUpdates(1)
                .build()
            return LocationRequest().apply {
                interval = locationInterval
                fastestInterval = locationInterval
                priority = LocationRequest.PRIORITY_HIGH_ACCURACY
                if (isLocationRequiredOnlyOneTime) numUpdates = 1
            }*/
        }
    }
}