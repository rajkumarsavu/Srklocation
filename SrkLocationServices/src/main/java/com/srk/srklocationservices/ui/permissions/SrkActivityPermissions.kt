package com.srk.srklocationservices.ui.permissions

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.checkSelfPermission
import java.lang.ref.WeakReference

class SrkActivityPermissions(
    private val context: Activity,
    private val permissions: Array<String>,
    private val srkLocationPermissionListener: SrkLocationPermissionListener,
    private val messageToDisplay: String? = null
) {
    //Create weak reference to hold instance
    private val activityWeakReference = WeakReference(context)

    companion object {
        private const val PERMISSION_REQUEST = 9553
        private const val SETTINGS_REQUEST = 9554
    }

    fun checkAllPermissionsGranted(context: Context, permissions: Array<String>) = permissions.all {
        checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
    }

    fun checkAnyOnePermissionDenied(context: Activity, permissions: Array<String>) =
        permissions.any {
            (checkSelfPermission(context, it) != PackageManager.PERMISSION_GRANTED)
                    && (ActivityCompat.shouldShowRequestPermissionRationale(context, it).not())
        }


    fun srkRequestPermissions() {
        if (activityWeakReference.get() == null) return
        if (permissions.isEmpty() || checkAllPermissionsGranted(context, permissions)) {
            srkLocationPermissionListener.permissionGranted()
        } else {
            ActivityCompat.requestPermissions(context, permissions, PERMISSION_REQUEST)
        }
    }

    fun onRequestPermissionsResult(requestCode: Int) {
        if (activityWeakReference.get() == null) return
        when (requestCode) {
            PERMISSION_REQUEST -> {
                when {
                    checkAllPermissionsGranted(context, permissions) -> {
                        srkLocationPermissionListener.permissionGranted()
                    }
                    checkAnyOnePermissionDenied(context, permissions) -> {
                        openAppPermissionSettings(context)
                    }
                    else -> {
                        srkLocationPermissionListener.permissionDenied()
                    }
                }
            }
        }
    }

    fun onActivityResult(requestCode: Int) {
        if (activityWeakReference.get() == null) return

        when (requestCode) {
            SETTINGS_REQUEST -> {
                if (checkAllPermissionsGranted(
                        context,
                        permissions
                    )
                ) srkLocationPermissionListener.permissionGranted()
                else srkLocationPermissionListener.permissionDenied()
            }
        }
    }


    private fun openAppPermissionSettings(
        activity: Activity
    ) {
        Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", activity.packageName, null)
            activity.startActivity(this)
        }
        Toast.makeText(activity, messageToDisplay, Toast.LENGTH_LONG).show()
    }
}