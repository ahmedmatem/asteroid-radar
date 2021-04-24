package com.udacity.asteroidradar.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.Constants
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

//private val moshi = Moshi.Builder()
//    .add(KotlinJsonAdapterFactory())
//    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(ScalarsConverterFactory.create())
//    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(Constants.BASE_URL)
    .build()

interface NeoApiService {
    @GET("neo/rest/v1/feed")
    fun getNextSevenDaysAsteroidsStartFrom(
        @Query("start_date") startDate: String,
        @Query("api_key") apiKey: String
    ): Call<String>
}

object NeoApi {
    val retrofitService: NeoApiService by lazy {
        retrofit.create(NeoApiService::class.java)
    }
}