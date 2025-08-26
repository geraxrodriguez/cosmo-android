package com.example.cosmo.model

import java.net.URL
import kotlinx.coroutines.*
import org.json.JSONObject
import org.json.JSONArray

class AsteroidRepository {
    suspend fun getAsteroids(date: String): List<Asteroid> {
        val url = URL("https://api.nasa.gov/neo/rest/v1/feed?start_date=$date&end_date=$date&api_key=lTHPxbpwqnn3thI5aiCieLtOpT1MZ85pxbkRI9tN")
        val response = url.readText()
        
        // Parse JSON response
        val jsonObject = JSONObject(response)
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
