package com.srk.srklocation

import android.location.Location
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.srk.srklocationservices.listners.OnLocationResultListner
import com.srk.srklocationservices.models.common.LocationNetworkStatus
import com.srk.srklocationservices.models.common.LocationResponse
import com.srk.srklocationservices.models.nearbyplaces.FormattedNearByPlaceModel
import com.srk.srklocationservices.models.placedetails.FormattedPlaceDetailsModel
import com.srk.srklocationservices.ui.locationapis.SRKLocationBuilder
import com.srk.srklocationservices.ui.locationservices.LocationEnum
import com.srk.srklocationservices.ui.locationservices.LocationListener
import com.srk.srklocationservices.ui.locationservices.SRKLocationUtilService

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //getNearByPlaces()
        //getPlaceDetails("ChIJA9RNruP1OzoROp8kxjO5dNk")
        //getAutoCompleteSearch("losan")

        getReverseGeoCoding()
    }

    override fun onResume() {
        super.onResume()
        //getCurrentLocation()
    }

    private fun getCurrentLocation() {
        SRKLocationUtilService.Builder(this, object : LocationListener {
            override fun locationOn() {
                Toast.makeText(this@MainActivity, "locationOn", Toast.LENGTH_SHORT).show()
            }

            override fun currentLocation(location: Location) {
                Toast.makeText(
                    this@MainActivity,
                    "currentLocation+" + location.latitude + "--" + location.longitude,
                    Toast.LENGTH_SHORT
                ).show()
            }

            override fun locationCancelled() {
                Toast.makeText(this@MainActivity, "locationCancelled", Toast.LENGTH_SHORT).show()
            }

            override fun onFailed(locationEnum: LocationEnum) {

            }
        }).build().getCurrentLocation()
    }

    private fun getAutoCompleteSearch(input: String) {
        val srkLocationBuilder = SRKLocationBuilder()
        srkLocationBuilder.googleApiKey("your api key")
        srkLocationBuilder.input(input)
        srkLocationBuilder.needResultInFormattedModel()

        srkLocationBuilder.onLocationResultListener(object : OnLocationResultListner {
            override fun onLocationDetailsFetched(locationResponse: LocationResponse) {
                when (locationResponse.networkStatus) {
                    LocationNetworkStatus.LOADING -> {
                        Toast.makeText(this@MainActivity, "loading", Toast.LENGTH_SHORT).show()
                    }
                    LocationNetworkStatus.SUCCESS -> {
                        val nearList = locationResponse.response as FormattedPlaceDetailsModel

                        Toast.makeText(
                            this@MainActivity, "SUCCESS+" + nearList.fullAddress, Toast.LENGTH_SHORT
                        ).show()

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
        }).buildAutoCompleteSearch().getAutoCompleteSearch()
    }

    private fun getNearByPlaces() {

        val srkLocationBuilder = SRKLocationBuilder()
        srkLocationBuilder.googleApiKey("your api key")
        srkLocationBuilder.locationLatLng(18.294830, 83.89366)
        srkLocationBuilder.radius(9000)
        srkLocationBuilder.needResultInFormattedModel()
        /*  srkLocationBuilder.language("in")
          srkLocationBuilder.minPrice(3)
          srkLocationBuilder.maxPrice(4)
          srkLocationBuilder.type("gas_station")*/

        srkLocationBuilder.onLocationResultListener(object : OnLocationResultListner {
            override fun onLocationDetailsFetched(locationResponse: LocationResponse) {
                when (locationResponse.networkStatus) {
                    LocationNetworkStatus.LOADING -> {
                        Toast.makeText(this@MainActivity, "loading", Toast.LENGTH_SHORT).show()
                    }
                    LocationNetworkStatus.SUCCESS -> {
                        val nearList =
                            locationResponse.response as ArrayList<FormattedNearByPlaceModel>

                        Toast.makeText(
                            this@MainActivity, "SUCCESS+" + nearList.size, Toast.LENGTH_SHORT
                        ).show()


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

    private fun getPlaceDetails(placeId: String) {
        val srkLocationBuilder = SRKLocationBuilder()
        srkLocationBuilder.googleApiKey("your api key")
        srkLocationBuilder.placeId(placeId)
        srkLocationBuilder.needResultInFormattedModel()

        srkLocationBuilder.onLocationResultListener(object : OnLocationResultListner {
            override fun onLocationDetailsFetched(locationResponse: LocationResponse) {
                when (locationResponse.networkStatus) {
                    LocationNetworkStatus.LOADING -> {
                        Toast.makeText(this@MainActivity, "loading", Toast.LENGTH_SHORT).show()
                    }
                    LocationNetworkStatus.SUCCESS -> {
                        val nearList = locationResponse.response as FormattedPlaceDetailsModel

                        Toast.makeText(
                            this@MainActivity, "SUCCESS+" + nearList.fullAddress, Toast.LENGTH_SHORT
                        ).show()

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
        }).buildPlaceDetails().getPlaceDetails()

    }

    private fun getReverseGeoCoding() {

        val srkLocationBuilder = SRKLocationBuilder()
        srkLocationBuilder.googleApiKey("AIzaSyClDhOnLiteNmmcsDjWcUbNB_NQp2iGb4U")
        srkLocationBuilder.locationLatLng(18.294830, 83.89366)
        srkLocationBuilder.needResultInFormattedModel()
        /*  srkLocationBuilder.language("in")
          srkLocationBuilder.minPrice(3)
          srkLocationBuilder.maxPrice(4)
          srkLocationBuilder.type("gas_station")*/

        srkLocationBuilder.onLocationResultListener(object : OnLocationResultListner {
            override fun onLocationDetailsFetched(locationResponse: LocationResponse) {
                when (locationResponse.networkStatus) {
                    LocationNetworkStatus.LOADING -> {
                        Toast.makeText(this@MainActivity, "loading", Toast.LENGTH_SHORT).show()
                    }
                    LocationNetworkStatus.SUCCESS -> {
                        val nearList =
                            locationResponse.response as FormattedPlaceDetailsModel

                        Toast.makeText(
                            this@MainActivity, "SUCCESS+" + nearList.fullAddress, Toast.LENGTH_SHORT
                        ).show()


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
        }).buildReverseGeoCoding().getReverseGeoCoding()
    }
}