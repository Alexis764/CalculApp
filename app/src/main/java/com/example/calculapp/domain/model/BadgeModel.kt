package com.example.calculapp.domain.model

data class BadgeModel (
    val success: Boolean,
    val badges: BadgesModel
)

data class BadgesModel(
    val usd: Double,
    val cop: Double,
    val mxn: Double
)