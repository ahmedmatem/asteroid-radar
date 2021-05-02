package com.udacity.asteroidradar.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.database.AsteroidsDatabase
import com.udacity.asteroidradar.database.asDomainModel
import com.udacity.asteroidradar.network.NeoApi
import com.udacity.asteroidradar.network.asDatabaseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.await
import java.lang.Exception

class PodRepository(private val database: AsteroidsDatabase) {

    val pod: LiveData<PictureOfDay> = Transformations.map(
        database.podDao.getPod(getTodayFormatted())
    ) {
        it?.asDomainModel()
    }

    suspend fun refreshPod(){
        withContext(Dispatchers.IO){
            val responseDeferred = NeoApi.retrofitService.getPod(Constants.API_KEY)
            try {
                val response = responseDeferred.await()
                val databasePod = response?.asDatabaseModel()
                database.podDao.insertPod(databasePod)
            } catch (e: Exception){
                throw Exception()
            }

        }
    }

    suspend fun deletePreviousDayPod(){
        withContext(Dispatchers.IO){
            database.podDao.deleteYesterdayPod(getYesterdayFormatted())
        }
    }
}