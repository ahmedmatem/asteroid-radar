package com.udacity.asteroidradar.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.PictureOfDay

@Entity(tableName = "asteroids")
data class DatabaseAsteroid(
    @PrimaryKey val id: Long,
    @ColumnInfo(name = "code_name") val codename: String,
    @ColumnInfo(name = "close_approach_date") val closeApproachDate: String,
    @ColumnInfo(name = "absolute_magnitude") val absoluteMagnitude: Double,
    @ColumnInfo(name = "estimated_diameter") val estimatedDiameter: Double,
    @ColumnInfo(name = "is_potentially_hazardous") val isPotentiallyHazardous: Boolean,
    @ColumnInfo(name = "relative_velocity") val relativeVelocity: Double,
    @ColumnInfo(name = "distance_from_earth") val distanceFromEarth: Double
)

@Entity(tableName = "pod")
data class DatabasePod(
    @PrimaryKey val date: String, // yyyy-MM-dd,
    @ColumnInfo(name = "media_type") val mediaType: String,
    val title: String,
    val url: String
)

// Extension function which converts from database objects to domain objects
fun DatabaseAsteroid.asDomainModel(): Asteroid {
    return Asteroid(
        id = id,
        codename = codename,
        closeApproachDate = closeApproachDate,
        absoluteMagnitude = absoluteMagnitude,
        estimatedDiameter = estimatedDiameter,
        relativeVelocity = relativeVelocity,
        distanceFromEarth = distanceFromEarth,
        isPotentiallyHazardous = isPotentiallyHazardous
    )
}

fun List<DatabaseAsteroid>.asDomainModel(): List<Asteroid> {
    return map {
        it.asDomainModel()
    }
}

fun DatabasePod.asDomainModel(): PictureOfDay
{
    return PictureOfDay(mediaType, title, url)
}

