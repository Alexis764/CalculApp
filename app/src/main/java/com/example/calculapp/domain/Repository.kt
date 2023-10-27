package com.example.calculapp.domain

import com.example.calculapp.data.network.response.BadgesResponse
import com.example.calculapp.domain.model.BadgeModel

interface Repository {
    suspend fun getObjectBadges(apiKey: String, symbols: String): BadgeModel?
}