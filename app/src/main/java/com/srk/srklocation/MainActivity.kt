package com.srk.srklocation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.srk.srklocationservices.listners.OnLocationResultListner
import com.srk.srklocationservices.models.common.LocationResponse
import com.srk.srklocationservices.models.common.LocationNetworkStatus
import com.srk.srklocationservices.ui.locationapis.SRKLocationBuilder

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val srkLocationBuilder = SRKLocationBuilder()
        srkLocationBuilder.googleApiKey("AIzaSyCYX8N_hZoyGVtM-4CsGit8zLPVb9aTNCks")
        srkLocationBuilder.locationLatLng(18.294830, 83.89366)
        srkLocationBuilder.radius(1)
        /*  srkLocationBuilder.language("in")
          srkLocationBuilder.minPrice(3)
          srkLocationBuilder.maxPrice(4)
          srkLocationBuilder.type("gas_station")*/

        srkLocationBuilder.onLocationResultListner(object : OnLocationResultListner {
            override fun onPlaceDetailsFetched(locationResponse: LocationResponse) {
                when (locationResponse.networkStatus) {
                    LocationNetworkStatus.LOADING -> {
                        Toast.makeText(this@MainActivity, "loading", Toast.LENGTH_SHORT).show()
                    }
                    LocationNetworkStatus.SUCCESS -> {
                        Toast.makeText(this@MainActivity, "SUCCESS", Toast.LENGTH_SHORT).show()

                    }
                    LocationNetworkStatus.ERROR_OR_FAIL -> {
                        Toast.makeText(
                            this@MainActivity,
                            "ERROR" + locationResponse.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    LocationNetworkStatus.EXCEPTION -> {
                        Toast.makeText(
                            this@MainActivity,
                            "EXCEPTION" + locationResponse.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    else -> {

                    }
                }
            }
        }).buildNearPlaces().getNearSearchPlaces()
    }
}