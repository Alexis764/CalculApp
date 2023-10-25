package com.example.calculapp.data.sqlite.model

data class CreditModel (
    val id: Int,
    val userIdentificationNumber: Long,
    val amountRequested: Int,
    val daysRequested: Int,
    val creditDate: String,
    val total: Int,
    var state: String
)