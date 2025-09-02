// Provides Repositories that depend on network services
// Keeps data access logic separate from networking and UI
package com.example.cosmo.di

import com.example.cosmo.model.AsteroidRepository
import com.example.cosmo.model.AsteroidRepositoryImpl
import com.example.cosmo.network.NasaApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module // Marks this class as a Hilt module
@InstallIn(SingletonComponent::class) // Makes these bindings live as long as app
object RepositoryModule {
    @Provides
    @Singleton
    fun providesAsteroidRepository(apiService: NasaApiService): AsteroidRepository {
        return AsteroidRepositoryImpl(apiService)
    }
}