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
import com.udacity.asteroidradar.repository.PodRepository
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception
import java.lang.IllegalArgumentException

enum class NeoApiStatus { LOADING, ERROR, DONE }

class MainViewModel(context: Context) : ViewModel() {
    private val _neoStatus = MutableLiveData<NeoApiStatus>()
    val neoStatus: LiveData<NeoApiStatus>
        get() = _neoStatus

    private val _navigateToSelectedAsteroidDetails = MutableLiveData<Asteroid?>()
    val navigateToSelectedAsteroidDetails: LiveData<Asteroid?>
        get() = _navigateToSelectedAsteroidDetails

    private val _filteredAsteroidList = MutableLiveData<List<Asteroid>>()
    val filteredAsteroidList: LiveData<List<Asteroid>>
        get() = _filteredAsteroidList

    private val database = getDatabase(context)
    private val asteroidsRepository = AsteroidsRepository(database)
    private val podRepository = PodRepository(database)

    private val viewModelJob = SupervisorJob()
    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    init {
        _neoStatus.value = NeoApiStatus.LOADING
        viewModelScope.launch {
            try {
                asteroidsRepository.refreshAsteroids()
                podRepository.refreshPod()
                _neoStatus.value = NeoApiStatus.DONE
            } catch (e: Exception) {
                _neoStatus.value = NeoApiStatus.ERROR
            }
        }
    }

    val asteroidList = asteroidsRepository.asteroids
    val pod = podRepository.pod

    fun displayAsteroidDetails(asteroid: Asteroid) {
        _navigateToSelectedAsteroidDetails.value = asteroid
    }

    fun displayAsteroidDetailsComplete() {
        _navigateToSelectedAsteroidDetails.value = null
    }

    fun updateFilteredAsteroids(filter: AsteroidsFilter) {
        viewModelScope.launch {
            _filteredAsteroidList.value = asteroidsRepository.getFilterAsteroids(filter)
        }
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