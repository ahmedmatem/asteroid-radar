package com.udacity.asteroidradar.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.network.NeoApi
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import timber.log.Timber

class MainViewModel : ViewModel() {
    private val _response = MutableLiveData<String>()

    val response : LiveData<String>
        get() = _response

    init {
        getAsteroids()
    }

    private fun getAsteroids() {
        NeoApi.retrofitService.getAsteroids()
            .enqueue(object : Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    response.body()?.let {
                        _response.value = response.body()
                    }
                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    // TODO("Check code before released it")
                    _response.value = "Failure: " + t.message
                }
            })
    }
}