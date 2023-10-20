package com.example.calculapp.domain.credit

data class CalculatedCreditModel (
    val amountRequested: Int,
    val daysRequested: Int,
    val interest: Int,
    val endorsement: Int,
    val endorsementDiscount: Int,
    val electronicSignature: Int,
    val discount: Int,
    val total: Int
)