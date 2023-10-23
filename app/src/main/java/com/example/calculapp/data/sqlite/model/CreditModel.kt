package com.example.calculapp.data.sqlite.model

data class CreditModel (
    val id: Int,
    val userIdentificationNumber: Int,
    val amountRequested: Int,
    val daysRequested: Int,
    val creditDate: String,
    val total: Int,
    val state: String
)