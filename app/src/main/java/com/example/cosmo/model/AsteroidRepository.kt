package com.example.cosmo.model

interface AsteroidRepository {
    suspend fun fetchAsteroids(date: String): List<Asteroid>
}