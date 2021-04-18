package com.udacity.asteroidradar.network

import com.udacity.asteroidradar.Constants
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET

private val retrofit = Retrofit.Builder()
    .addConverterFactory(ScalarsConverterFactory.create())
    .baseUrl(Constants.NEO_API_BASE_URL)
    .build()

interface NeoApiService {
    @GET("browse")
    fun getAsteroids() : Call<String>
}

object NeoApi {
    val retrofitService : NeoApiService by lazy {
        retrofit.create(NeoApiService::class.java)
    }
}