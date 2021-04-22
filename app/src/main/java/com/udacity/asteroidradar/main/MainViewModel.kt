package com.udacity.asteroidradar.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.network.NeoApi
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

class MainViewModel : ViewModel() {
    private val _response = MutableLiveData<String>()

    val response: LiveData<String>
        get() = _response

    init {
        getNextSevenDaysAsteroids()
    }

    private fun getNextSevenDaysAsteroids() {
        val dateFormat = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())
        val now = Calendar.getInstance().time
        // set startDate from now
        val startDate = dateFormat.format(now)

        NeoApi.retrofitService.getNextSevenDaysAsteroidsStartFrom(startDate, Constants.NASA_API_KEY)
            .enqueue(object : Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    response.body()?.let {
                        _response.value = parseAsteroidsJsonResult(JSONObject(response.body()))
                            .size.toString()
                    }
                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    // TODO("Check code before released it")
                    _response.value = "Failure: " + t.message
                }
            })
    }
}