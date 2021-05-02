package com.udacity.asteroidradar.repository

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
import java.lang.Exception

enum class AsteroidsFilter() { TODAY, WEEK, SAVED }

class AsteroidsRepository(private val database: AsteroidsDatabase) {

    /**
     * List of asteroids that can be shown on the screen
     */

    val asteroids: LiveData<List<Asteroid>> = Transformations.map(
        database.asteroidDao.getAsteroids(getTodayFormatted())
    ) {
        it?.asDomainModel()
    }

    suspend fun getFilterAsteroids(filter: AsteroidsFilter): List<Asteroid> {
        return withContext(Dispatchers.IO) {
            when (filter) {
                AsteroidsFilter.TODAY -> {
                    database.asteroidDao.getTodayAsteroids(getTodayFormatted())?.asDomainModel()
                }
                AsteroidsFilter.WEEK -> {
                    database.asteroidDao.getWeekAsteroids(getTodayFormatted())?.asDomainModel()
                }
                else -> {
                    database.asteroidDao.getSavedAsteroids()?.asDomainModel()
                }
            }
        }
    }

    suspend fun refreshAsteroids() {
        withContext(Dispatchers.IO) {
            val responseDeferred = NeoApi.retrofitService
                .getNextSevenDaysAsteroids(Constants.API_KEY)
            try {
                val response = responseDeferred.await()
                val asteroids = parseAsteroidsJsonResult(JSONObject(response))
                database.asteroidDao.insertAllAsteroids(*asteroids.asDatabaseModel())
            } catch (e: Exception) {
                throw Exception()
            }

        }
    }

    suspend fun deletePreviousDayAsteroids() {
        withContext(Dispatchers.IO) {
            database.asteroidDao.deleteYesterdayAsteroids(getYesterdayFormatted())
        }
    }
}