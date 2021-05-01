package com.udacity.asteroidradar.main

import android.content.Context
import androidx.lifecycle.*
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.database.getDatabase
import com.udacity.asteroidradar.network.NeoApi
import com.udacity.asteroidradar.repository.AsteroidsFilter
import com.udacity.asteroidradar.repository.AsteroidsRepository
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.IllegalArgumentException

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

    private val _filter = MutableLiveData<AsteroidsFilter>()
    val filter: LiveData<AsteroidsFilter>
        get() = _filter

    private val database = getDatabase(context)
    private val asteroidsRepository = AsteroidsRepository(database)

    private val viewModelJob = SupervisorJob()
    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    init {
        viewModelScope.launch {
            asteroidsRepository.refreshAsteroids()
            getPictureOfDay()
        }
    }

    var asteroidList = asteroidsRepository.asteroids

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

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
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