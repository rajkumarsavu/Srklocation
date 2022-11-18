package com.srk.srklocationservices.api

import com.srk.srklocationservices.BuildConfig
import com.srk.srklocationservices.utils.AppConstants
import com.srk.srklocationservices.utils.retrofitGson
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit

class RetrofitServiceUtil {

    private fun <T> getRetrofitInterface(webserviceInterface: Class<T>): T {
        val builder: OkHttpClient.Builder = OkHttpClient.Builder().callTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS).readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS).retryOnConnectionFailure(true)
        if (BuildConfig.BUILD_TYPE != "release") {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            builder.addInterceptor(interceptor)
        }
        val retrofit =
            Retrofit.Builder().client(builder.build()).baseUrl(AppConstants.GOOGLE_SERVICE_API)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(retrofitGson))
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create()).build()
        return retrofit.create(webserviceInterface)
    }

    fun locationAPIService(): LocationApiService =
        getRetrofitInterface(LocationApiService::class.java)

}
