package com.example.cosmo.network

import retrofit2.http.GET
import retrofit2.http.Query
import okhttp3.ResponseBody

interface NasaApiService {
    @GET("neo/rest/v1/feed")
    suspend fun getAsteroids(
        @Query("start_date") startDate: String,
        @Query("end_date") endDate: String,
        @Query("api_key") apiKey: String
    ): ResponseBody
}