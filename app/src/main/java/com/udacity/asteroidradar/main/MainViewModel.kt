package com.udacity.asteroidradar.main

import android.content.Context
import androidx.lifecycle.*
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.api.Result
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.database.getDatabase
import com.udacity.asteroidradar.network.NeoApi
import com.udacity.asteroidradar.network.asDatabaseModel
import com.udacity.asteroidradar.repository.AsteroidsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
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

    private val _navigateToSelectedAsteroidDetails = MutableLiveData<Asteroid?>()
    val navigateToSelectedAsteroidDetails: LiveData<Asteroid?>
        get() = _navigateToSelectedAsteroidDetails

    private val viewModelJob = SupervisorJob()
    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val database = getDatabase(context)
    private val asteroidsRepository = AsteroidsRepository(database)

    init {
        viewModelScope.launch {
            _neoStatus.value = NeoApiStatus.LOADING
            asteroidsRepository.refreshAsteroids()
            _neoStatus.value = NeoApiStatus.DONE
        }

        getPictureOfDay()
    }

//    val asteroidList = asteroidsRepository.asteroids
    val asteroidList = asteroidsRepository.weekAsteroids

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
                    call: Call<PictureOfDay>, response: Response<PictureOfDay>
                ) {
                    if (response.isSuccessful) {
                        response.body()?.let {
                            _pictureOfDay.value = it
                        }
                    }
                }

                override fun onFailure(call: Call<PictureOfDay>, t: Throwable) {
                    _pictureOfDay.value = PictureOfDay(mediaType = "video", "", "")
                }
            })
    }

    /**
     * Factory to construct MainViewModel with parameter
     */
    class Factory(private val context: Context) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                return MainViewModel(context) as T
            }
            throw IllegalArgumentException("Unable to construct ViewModel.")
        }
    }

}