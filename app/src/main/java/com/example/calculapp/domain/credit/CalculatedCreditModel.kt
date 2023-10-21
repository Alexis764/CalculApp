package com.example.calculapp.domain.credit

import java.time.LocalDate

data class CalculatedCreditModel (
    val amountRequested: Int,
    val daysRequested: Int,
    val creditDate: LocalDate,
    val interest: Int,
    val endorsement: Int,
    val endorsementDiscount: Int,
    val electronicSignature: Int,
    val discount: Int,
    val total: Int
)