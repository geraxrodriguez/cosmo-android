package com.example.cosmo.model

import com.example.cosmo.network.NasaApiService
import com.squareup.moshi.Moshi
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

// Repo implementation using Moshi for JSON parsing
class AsteroidRepositoryImpl (
    private val apiService: NasaApiService
) : AsteroidRepository {

    // Moshi instance with Kotlin support
    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory()) // Adds Kotlin support (null safety, default params)
        .build()

    private val adapter = moshi.adapter(AsteroidResponse::class.java)

    override suspend fun fetchAsteroids(date: String): List<Asteroid> {
        // Call API and get raw JSON string
        val response = apiService.getAsteroids(
            date,
            date,
            "lTHPxbpwqnn3thI5aiCieLtOpT1MZ85pxbkRI9tN"
        )
        val jsonString = response.string()

        // Deserialize JSON into AsteroidResponse DTO
        val asteroidResponse = adapter.fromJson(jsonString) ?: return emptyList() // Return empty list if parsing fails

        // Extract the list for the specific date
        val asteroidsDto = asteroidResponse.nearEarthObjects[date] ?: emptyList()

        // Map DTOs to domain model
        return asteroidsDto.map { dto ->
            Asteroid(
                name = dto.name.replace("[()]".toRegex(), ""),
                diameter = dto.estimatedDiameter.feet.estimatedDiameterMax.toInt(),
                velocity = dto.closeApproachData.firstOrNull()?.relativeVelocity?.milesPerHour?.toDouble()?.toInt() ?: 0,
                missDistance = dto.closeApproachData.firstOrNull()?.missDistance?.miles?.toDouble()?.toInt() ?: 0,
                isHazardous = dto.isPotentiallyHazardousAsteroid,
            )
        }
    }


}

// -------------------------- DTOs --------------------------
@JsonClass(generateAdapter = true)
data class AsteroidResponse(
    @Json(name = "near_earth_objects")
    val nearEarthObjects: Map<String, List<AsteroidDto>>
)

@JsonClass(generateAdapter = true)
data class AsteroidDto(
    val name: String,
    @Json(name = "estimated_diameter")
    val estimatedDiameter: EstimatedDiameter,
    @Json(name = "is_potentially_hazardous_asteroid")
    val isPotentiallyHazardousAsteroid: Boolean,
    @Json(name = "close_approach_data")
    val closeApproachData: List<CloseApproach>
)

@JsonClass(generateAdapter = true)
data class EstimatedDiameter(
    val feet: DiameterFeet
)

@JsonClass(generateAdapter = true)
data class DiameterFeet(
    @Json(name = "estimated_diameter_max")
    val estimatedDiameterMax: Double
)

@JsonClass(generateAdapter = true)
data class CloseApproach(
    @Json(name = "relative_velocity")
    val relativeVelocity: RelativeVelocity,
    @Json(name = "miss_distance")
    val missDistance: MissDistance
)

@JsonClass(generateAdapter = true)
data class RelativeVelocity(
    @Json(name = "miles_per_hour")
    val milesPerHour: String
)

@JsonClass(generateAdapter = true)
data class MissDistance(
    val miles: String
)