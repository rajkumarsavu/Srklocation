package com.srk.srklocationservices.ui.locationapis

import com.srk.srklocationservices.api.RetrofitServiceUtil
import com.srk.srklocationservices.listners.OnLocationResultListner
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SRKLocationsServices {

    companion object {

        /*fun getAutoCompletePlaces(onLocationListner: OnLocationResultListner) {

            onLocationListner.onPlaceDetailsFetched(loading())
            val response = RetrofitServiceUtil().locationAPIService()
                .getNearPlaces("api/place/nearbysearch/json?location=18.294830,%2083.893661&radius=9000&key=AIzaSyCYX8N_hZoyGVtM-4CsGit8zLPVb9aTNCk")

            response.enqueue(object : Callback<ResponseBody?> {
                override fun onResponse(
                    call: Call<ResponseBody?>, response: Response<ResponseBody?>
                ) {
                    try {
                        if (response.isSuccessful && response.body() != null) {
                            val responseBody = response.body()
                            onLocationListner.onPlaceDetailsFetched(success("", responseBody))
                        } else if (response.errorBody() != null) {
                            onLocationListner.onPlaceDetailsFetched(error("", response.errorBody()))
                        } else {
                            onLocationListner.onPlaceDetailsFetched(failure("", null))
                        }
                    } catch (e: Exception) {
                        onLocationListner.onPlaceDetailsFetched(failure("", e.message))
                    }
                }

                override fun onFailure(call: Call<ResponseBody?>, e: Throwable) {
                    onLocationListner.onPlaceDetailsFetched(failure("", e.message))
                }
            })


        }*/


    }


}