package com.udacity.asteroidradar.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.api.parseApodJsonResult
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.network.NeoApi
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MainViewModel : ViewModel() {
    private val _pictureOfDay = MutableLiveData<PictureOfDay>()
    private val _asteroidList = MutableLiveData<ArrayList<Asteroid>>()

    val pictureOfDay: LiveData<PictureOfDay>
        get() = _pictureOfDay

    val asteroidList: LiveData<ArrayList<Asteroid>>
        get() = _asteroidList

    init {
        getPictureOfDay()
        getNextSevenDaysAsteroids()
    }

    private fun getPictureOfDay() {
        NeoApi.retrofitService.getApod(Constants.API_KEY)
            .enqueue(object : Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    if (response.isSuccessful) {
                        response.body()?.let {
                            _pictureOfDay.value = parseApodJsonResult(JSONObject(it))
                        }
                    }
                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    // TODO("Check code before released it")
                }
            })
    }

    private fun getNextSevenDaysAsteroids() {
        val dateFormat = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())
        val now = Calendar.getInstance().time
        // set startDate from now
        val startDate = dateFormat.format(now)

        NeoApi.retrofitService.getNextSevenDaysAsteroidsStartFrom(startDate, Constants.API_KEY)
            .enqueue(object : Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    response.body()?.let {
                        _asteroidList.value = parseAsteroidsJsonResult(JSONObject(response.body()))
                    }
                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    // TODO("Check code before released it")
                }
            })
    }
}