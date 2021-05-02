package com.udacity.asteroidradar.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.PictureOfDay

@Dao
interface AsteroidDao {
    @Query("SELECT * FROM asteroids WHERE close_approach_date = :today ORDER BY close_approach_date")
    fun getAsteroids(today: String): LiveData<List<DatabaseAsteroid>>

    @Query("SELECT * FROM asteroids WHERE close_approach_date >= :today ORDER BY close_approach_date")
    fun getWeekAsteroids(today: String): List<DatabaseAsteroid>

    @Query("SELECT * FROM asteroids WHERE close_approach_date = :today ORDER BY close_approach_date")
    fun getTodayAsteroids(today: String) : List<DatabaseAsteroid>

    @Query("SELECT * FROM asteroids ORDER BY close_approach_date")
    fun getSavedAsteroids(): List<DatabaseAsteroid>

    @Query("DELETE FROM asteroids WHERE close_approach_date = :yesterday")
    fun deleteYesterdayAsteroids(yesterday: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllAsteroids(vararg asteroids: DatabaseAsteroid)
}

@Dao
interface PodDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPod(pod: DatabasePod)

    @Query("SELECT * FROM pod WHERE date = :today LIMIT 1")
    fun getPod(today: String) : LiveData<DatabasePod>

    @Query("DELETE FROM pod WHERE date = :yesterday")
    fun deleteYesterdayPod(yesterday: String)
}

@Database(entities = [DatabaseAsteroid::class, DatabasePod::class], version = 2, exportSchema = false)
abstract class AsteroidsDatabase : RoomDatabase() {
    abstract val asteroidDao: AsteroidDao
    abstract val podDao: PodDao
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