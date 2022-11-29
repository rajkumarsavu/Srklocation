package com.srk.srklocationservices.ui.locationservices

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.srk.srklocationservices.ui.locationservices.helper.SRKGooglePlayApiHelper
import com.srk.srklocationservices.ui.locationservices.helper.SRKLocationSettingAndRequestHelper
import com.srk.srklocationservices.ui.permissions.SrkActivityPermissions
import com.srk.srklocationservices.ui.permissions.SrkLocationPermissionListener
import java.lang.ref.WeakReference


class SRKLocationUtilService(private val builder: Builder) {

    private var fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(builder.getContext())
    private var srkGooglePlayApiHelper: SRKGooglePlayApiHelper
    private var srkLocationSettingAndRequestHelper: SRKLocationSettingAndRequestHelper
    private var srkPermission: SrkActivityPermissions

    private val activityWeakReference = WeakReference(builder.getContext())

    init {

        //Initiate Location setting API
        srkLocationSettingAndRequestHelper = SRKLocationSettingAndRequestHelper(builder,
            object : SRKLocationSettingAndRequestHelper.Companion.Listener {
                override fun onSuccess() {
                    getFusedLocation()
                }

                override fun onFailed(locationEnum: LocationEnum) {
                    if (activityWeakReference.get() == null) return
                    builder.getLocationListener().onFailed(locationEnum)
                }
            })

        //Initiate Activity Permission
        srkPermission = SrkActivityPermissions((builder.getContext() as Activity), arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION
        ),
            object : SrkLocationPermissionListener {
                override fun permissionGranted() {
                    srkLocationSettingAndRequestHelper.checkLocationSettingsIfNotShow()
                }

                override fun permissionDenied() {
                    builder.getLocationListener()
                        .onFailed(LocationEnum.LOCATION_PERMISSION_NOT_GRANTED)
                }

            })

        //Initiate Google play helper
        srkGooglePlayApiHelper =
            SRKGooglePlayApiHelper(builder, object : SRKGooglePlayApiHelper.Companion.Listener {
                override fun onSuccess() {
                    if (activityWeakReference.get() == null) return
                    srkPermission.srkRequestPermissions()

                }

                override fun onFailed() {
                    if (activityWeakReference.get() == null) return
                    builder.getLocationListener()
                        .onFailed(LocationEnum.GOOGLE_PLAY_API_NOT_AVAILABLE)
                }

            })

    }


    fun getCurrentLocation() {
        if (activityWeakReference.get() == null) return
        builder.locationFrequently(false)
        srkGooglePlayApiHelper.checkAndMakeAvailableGooglePlayServices()
    }


    fun getLocationFrequently() {
        if (activityWeakReference.get() == null) return
        builder.locationFrequently(true)
        srkGooglePlayApiHelper.checkAndMakeAvailableGooglePlayServices()
    }

    private fun getFusedLocation() {
        if (ActivityCompat.checkSelfPermission(
                builder.getContext(), Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                builder.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fusedLocationClient.lastLocation.addOnSuccessListener { location -> // Got last known location. In some rare situations this can be null.
            if (location != null) {
                builder.getLocationListener().currentLocation(location)
                if (builder.getLocationFrequently()) {
                    startLocationUpdates()
                }
            } else {
                startLocationUpdates()
            }
        }.addOnFailureListener {
            startLocationUpdates()
        }.addOnCanceledListener {
            builder.getLocationListener().locationCancelled()
        }
    }

    private fun startLocationUpdates() {
        srkLocationSettingAndRequestHelper.addLifecycleListener(fusedLocationClient)
    }

    private fun stopLocationUpdates() {
        srkLocationSettingAndRequestHelper.stopLocationUpdates(fusedLocationClient)
    }

    //Builder
    data class Builder(
        private val context: Context, private val locationListener: LocationListener
    ) {

        fun getContext() = context
        fun getLocationListener() = locationListener

        private var locationFrequently = false
        fun getLocationFrequently() = locationFrequently
        fun locationFrequently(frequently: Boolean): Builder {
            locationFrequently = frequently
            return this
        }

        private var locationRequest: LocationRequest? = null
        fun getLocationRequest() = locationRequest
        fun locationRequest(locationRequestVal: LocationRequest): Builder {
            locationRequest = locationRequestVal
            return this
        }

        fun build(): SRKLocationUtilService {
            return SRKLocationUtilService(
                this
            )
        }
    }

    fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (activityWeakReference.get() == null) return
        srkPermission.onRequestPermissionsResult(requestCode)
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (activityWeakReference.get() == null) return
        srkPermission.onActivityResult(requestCode)
        srkLocationSettingAndRequestHelper.onActivityResult(requestCode, resultCode, data)
        srkGooglePlayApiHelper.onActivityResult(requestCode)
    }
}