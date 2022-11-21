package com.srk.srklocationservices.api

import com.srk.srklocationservices.models.nearbyplaces.NearBySearchResponse
import com.srk.srklocationservices.models.placedetails.PlaceDetailsResponse
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Url


interface LocationApiService {

    @GET
    fun getAutoCompletePlaces(@Url url: String?): Response<ResponseBody>

    @GET
    fun getNearPlaces(@Url url: String?): Call<NearBySearchResponse>

    @GET
    fun getPlaceDetailsByPlaceId(@Url url: String?): Call<PlaceDetailsResponse>


}
