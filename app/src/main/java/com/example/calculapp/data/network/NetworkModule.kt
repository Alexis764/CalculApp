package com.example.calculapp.data.network

import com.example.calculapp.data.RepositoryImpl
import com.example.calculapp.domain.Repository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit
            .Builder()
            .baseUrl("http://data.fixer.io/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    fun provideSettingsApiService(retrofit: Retrofit): SettingsApiService {
        return retrofit.create(SettingsApiService::class.java)
    }

    @Provides
    fun provideRepository(settingsApiService: SettingsApiService): Repository {
        return RepositoryImpl(settingsApiService)
    }

}