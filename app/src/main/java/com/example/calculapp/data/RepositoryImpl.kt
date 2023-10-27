package com.example.calculapp.data

import android.util.Log
import com.example.calculapp.data.network.SettingsApiService
import com.example.calculapp.domain.Repository
import com.example.calculapp.domain.model.BadgeModel
import javax.inject.Inject

class RepositoryImpl @Inject constructor(private val settingsApiService: SettingsApiService): Repository {

    override suspend fun getObjectBadges(apiKey: String, symbols: String): BadgeModel? {
        runCatching { settingsApiService.getBadges(apiKey, symbols) }
            .onSuccess { return it.toDomain() }
            .onFailure { Log.i("getObjectBadges", "Ha ocurrido un error ${it.message}") }

        return null
    }

}