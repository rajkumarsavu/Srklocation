package com.srk.srklocationservices.ui.locationservices.helper

import android.app.Activity
import androidx.fragment.app.Fragment
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.srk.srklocationservices.ui.locationservices.SRKLocationUtilService
import java.lang.ref.WeakReference

class SRKGooglePlayApiHelper(
    builder: SRKLocationUtilService.Builder,
    private val listener: Listener
) {
    private val weakReference = WeakReference(builder.getContext())

    fun checkAndMakeAvailableGooglePlayServices() {
        weakReference.get()?.let { contextTemp ->
            val apiServiceAvailability =
                GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(contextTemp)
            if (apiServiceAvailability == ConnectionResult.SUCCESS) {
                if (weakReference.get() == null) {
                    return
                }
                listener.onSuccess()
            } else {
                if (contextTemp is Activity) {
                    val errorDialog = GoogleApiAvailability.getInstance().getErrorDialog(
                        contextTemp, apiServiceAvailability, REQUEST_CODE
                    ) {
                        listener.onFailed()
                    }
                    errorDialog?.show()
                } else if (contextTemp is Fragment) {
                    val errorDialog = GoogleApiAvailability.getInstance().getErrorDialog(
                        (contextTemp as Fragment), apiServiceAvailability, REQUEST_CODE
                    ) {
                        listener.onFailed()
                    }
                    errorDialog?.show()
                } else {

                }

            }
        }

    }

    fun onActivityResult(requestCode: Int) {
        if (weakReference.get() == null) return
        if (requestCode == REQUEST_CODE) {
            checkAndMakeAvailableGooglePlayServices()
        }
    }

    companion object {
        private const val REQUEST_CODE = 9907

        interface Listener {
            fun onSuccess()
            fun onFailed()
        }
    }
}