package com.example.calculapp.data.network

import com.example.calculapp.data.network.response.BadgesResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface SettingsApiService {

    @GET("/latest")
    suspend fun getBadges(
        @Query("access_key") apiKey: String,
        @Query("symbols", encoded = true) symbols: String
    ): BadgesResponse

}