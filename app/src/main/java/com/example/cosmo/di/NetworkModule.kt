// this file is meant to PROVIDE OkHttpClient, Retrofit, and the API services.
package com.example.cosmo.di

import com.example.cosmo.network.NasaApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import javax.inject.Singleton

@Module // marks this class as a Hilt module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    private const val BASE_URL = "https://api.nasa.gov/"

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpclient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpclient)
            .build()
    }

    @Provides
    @Singleton
    fun provideNasaApiService(retrofit: Retrofit): NasaApiService {
        return retrofit.create(NasaApiService::class.java)
    }
}
