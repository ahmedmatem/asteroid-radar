package com.udacity.asteroidradar.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.udacity.asteroidradar.database.getDatabase
import com.udacity.asteroidradar.repository.AsteroidsRepository
import com.udacity.asteroidradar.repository.PodRepository
import retrofit2.HttpException

class RefreshDataWorker(context: Context, params: WorkerParameters) :
    CoroutineWorker(context, params) {
    companion object {
        const val WORK_NAME = "RefreshDataWork"
    }

    override suspend fun doWork(): Result {
        val database = getDatabase(applicationContext)
        val asteroidsRepository = AsteroidsRepository(database)
        val podRepository = PodRepository(database)

        return try {
            // refresh
            asteroidsRepository.refreshAsteroids()
            podRepository.refreshPod()
            // clear
            asteroidsRepository.deletePreviousDayAsteroids()
            podRepository.deletePreviousDayPod()
            Result.success()
        } catch (exc: HttpException) {
            Result.retry()
        }
    }
}