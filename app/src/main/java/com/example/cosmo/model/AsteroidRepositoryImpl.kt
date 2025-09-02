package com.example.cosmo.model

import com.example.cosmo.network.NasaApiService
import org.json.JSONObject

class AsteroidRepositoryImpl(
    private val apiService: NasaApiService
) : AsteroidRepository {


    override suspend fun fetchAsteroids(date: String): List<Asteroid> {
        val response = apiService.getAsteroids(date, date,"lTHPxbpwqnn3thI5aiCieLtOpT1MZ85pxbkRI9tN")
        val jsonString = response.string()

        // Parse JSON response
        val jsonObject = JSONObject(jsonString)
        val nearEarthObjects = jsonObject.getJSONObject("near_earth_objects")
        val asteroidsArray = nearEarthObjects.getJSONArray(date)

        val asteroids = mutableListOf<Asteroid>()

        for (i in 0 until asteroidsArray.length()) {
            val asteroidObject = asteroidsArray.getJSONObject(i)
            val closeApproachData = asteroidObject.getJSONArray("close_approach_data")
            val firstApproach = closeApproachData.getJSONObject(0)

            val asteroid = Asteroid(
                name = asteroidObject.getString("name").replace("[()]".toRegex(), ""),
                diameter = asteroidObject.getJSONObject("estimated_diameter")
                    .getJSONObject("feet")
                    .getDouble("estimated_diameter_max")
                    .toInt(),
                velocity = firstApproach.getJSONObject("relative_velocity")
                    .getString("miles_per_hour")
                    .toDouble()
                    .toInt(),
                missDistance = firstApproach.getJSONObject("miss_distance")
                    .getString("miles")
                    .toDouble()
                    .toInt(),
                isHazardous = asteroidObject.getBoolean("is_potentially_hazardous_asteroid")
            )
            asteroids.add(asteroid)
        }

        return asteroids
    }
}
