package com.udacity.asteroidradar.repository

import android.text.format.DateUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.database.AsteroidsDatabase
import com.udacity.asteroidradar.database.asDomainModel
import com.udacity.asteroidradar.network.NeoApi
import com.udacity.asteroidradar.network.asDatabaseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import retrofit2.await

class AsteroidsRepository(private val database: AsteroidsDatabase) {

    /**
     * A list of asteroids that can be shown on the screen
     */
    val asteroids: LiveData<List<Asteroid>> =
        Transformations.map(database.asteroidDao.getAsteroids()) {
            it.asDomainModel()
        }

    val weekAsteroids: LiveData<List<Asteroid>> = Transformations.map(
        database.asteroidDao.getWeekAsteroids(getTodayFormatted())){
            it.asDomainModel()
        }

    suspend fun refreshAsteroids() {
        withContext(Dispatchers.IO) {
            val response = NeoApi.retrofitService
                .getNextSevenDaysAsteroidsStartFrom(Constants.API_KEY).await()
            val asteroids = parseAsteroidsJsonResult(JSONObject(response))
            database.asteroidDao.insertAll(*asteroids.asDatabaseModel())
        }
    }
}