package com.udacity.asteroidradar.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*
import com.udacity.asteroidradar.Asteroid

@Dao
interface AsteroidDao {
    @Query("SELECT * FROM asteroids WHERE close_approach_date >= :today ORDER By close_approach_date")
    fun getWeekAsteroids(today: String): LiveData<List<DatabaseAsteroid>>

    @Query("SELECT * FROM asteroids WHERE close_approach_date = :today ORDER BY close_approach_date")
    fun getTodayAsteroids(today: String) : LiveData<List<DatabaseAsteroid>>

    @Query("SELECT * FROM asteroids ORDER BY close_approach_date")
    fun getSavedAsteroids(): LiveData<List<DatabaseAsteroid>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg asteroids: DatabaseAsteroid)
}

@Database(entities = [DatabaseAsteroid::class], version = 1, exportSchema = false)
abstract class AsteroidsDatabase : RoomDatabase() {
    abstract val asteroidDao: AsteroidDao
}

private lateinit var INSTANCE: AsteroidsDatabase

fun getDatabase(context: Context): AsteroidsDatabase {
    synchronized(AsteroidsDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(
                context.applicationContext,
                AsteroidsDatabase::class.java,
                "asteroids"
            )
                .build()
        }
    }
    return INSTANCE
}