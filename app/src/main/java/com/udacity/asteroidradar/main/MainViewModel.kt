package com.udacity.asteroidradar.main

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.database.AsteroidsDatabase
import com.udacity.asteroidradar.database.getDatabase
import com.udacity.asteroidradar.network.NeoApi
import com.udacity.asteroidradar.repository.AsteroidsRepository
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.IllegalArgumentException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

enum class NeoApiStatus { LOADING, ERROR, DONE }

class MainViewModel(context: Context) : ViewModel() {
    private val _neoStatus = MutableLiveData<NeoApiStatus>()
    val neoStatus: LiveData<NeoApiStatus>
        get() = _neoStatus

    private val _pictureOfDay = MutableLiveData<PictureOfDay>()
    val pictureOfDay: LiveData<PictureOfDay>
        get() = _pictureOfDay

    private val _asteroidList = MutableLiveData<ArrayList<Asteroid>>()
    val asteroidList: LiveData<ArrayList<Asteroid>>
        get() = _asteroidList

    private val _navigateToSelectedAsteroidDetails = MutableLiveData<Asteroid?>()
    val navigateToSelectedAsteroidDetails: LiveData<Asteroid?>
        get() = _navigateToSelectedAsteroidDetails

    private val database = getDatabase(context)
    private val repository = AsteroidsRepository(database)

    init {
        getPictureOfDay()
        getNextSevenDaysAsteroids()
    }

    fun displayAsteroidDetails(asteroid: Asteroid) {
        _navigateToSelectedAsteroidDetails.value = asteroid
    }

    fun displayAsteroidDetailsComplete() {
        _navigateToSelectedAsteroidDetails.value = null
    }

    private fun getPictureOfDay() {
        NeoApi.retrofitService.getApod(Constants.API_KEY)
            .enqueue(object : Callback<PictureOfDay> {
                override fun onResponse(
                    call: Call<PictureOfDay>,
                    response: Response<PictureOfDay>
                ) {
                    if (response.isSuccessful) {
                        response.body()?.let {
                            _pictureOfDay.value = it
                        }
                    }
                }

                override fun onFailure(call: Call<PictureOfDay>, t: Throwable) {
                    // TODO("Check code before released it")
                }
            })
    }

    private fun getNextSevenDaysAsteroids() {
        val dateFormat = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())
        val now = Calendar.getInstance().time
        // set startDate from now
        val startDate = dateFormat.format(now)

        _neoStatus.value = NeoApiStatus.LOADING
        NeoApi.retrofitService.getNextSevenDaysAsteroidsStartFrom(startDate, Constants.API_KEY)
            .enqueue(object : Callback<String> {
                override fun onResponse(call: Call<String>, response: Response<String>) {
                    response.body()?.let {
                        _neoStatus.value = NeoApiStatus.DONE
                        _asteroidList.value = parseAsteroidsJsonResult(JSONObject(response.body()))
                    }
                }

                override fun onFailure(call: Call<String>, t: Throwable) {
                    _neoStatus.value = NeoApiStatus.ERROR
                    _asteroidList.value = ArrayList()
                }
            })
    }

    /**
     * Factory to construct MainViewModel with parameter
     */
    class Factory(val context: Context) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if(modelClass.isAssignableFrom(MainViewModel::class.java)){
                return MainViewModel(context) as T
            }
            throw IllegalArgumentException("Unable to construct ViewModel.")
        }
    }

}