package com.udacity.asteroidradar.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.udacity.asteroidradar.Constants
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(Constants.NEO_API_BASE_URL)
    .build()

interface NeoApiService {
    @GET("browse?api_key=" + Constants.NASA_API_KEY)
    fun getAsteroids(): Call<String>
}

object NeoApi {
    val retrofitService: NeoApiService by lazy {
        retrofit.create(NeoApiService::class.java)
    }
}