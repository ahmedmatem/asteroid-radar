package com.udacity.asteroidradar.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.database.AsteroidsDatabase
import com.udacity.asteroidradar.database.asDomainModel

class PodRepository(private val database: AsteroidsDatabase) {

    val pod: LiveData<PictureOfDay> = Transformations.map(
        database.podDao.getPod(getTodayFormatted())
    ) {
        it.asDomainModel()
    }
}