package com.udacity.asteroidradar.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {
    private val _response = MutableLiveData<String>()

    val response : LiveData<String>
        get() = _response

    init {
        getAsteroids()
    }

    private fun getAsteroids() {
        _response.value = "Not implemented yet!"
    }
}