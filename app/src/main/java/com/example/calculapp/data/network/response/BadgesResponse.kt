package com.example.calculapp.data.network.response

import com.example.calculapp.domain.model.BadgeModel
import com.example.calculapp.domain.model.BadgesModel
import com.google.gson.annotations.SerializedName

data class BadgesResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("rates") val badges: Badges
) {
    fun toDomain(): BadgeModel {
        return BadgeModel(
            success = success,
            badges = toDomainBadges()
        )
    }

    private fun toDomainBadges(): BadgesModel {
        return BadgesModel(
            usd = badges.usd,
            cop = badges.cop,
            mxn = badges.mxn
        )
    }
}

data class Badges(
    @SerializedName("USD") val usd: Double,
    @SerializedName("COP") val cop: Double,
    @SerializedName("MXN") val mxn: Double
)