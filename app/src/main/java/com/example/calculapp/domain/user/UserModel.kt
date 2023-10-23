package com.example.calculapp.domain.user

data class UserModel (
    val name: String,
    val lastName: String,
    val documentType: String,
    val identificationNumber: Long,
    val email: String,
    val phoneNumber: Long,
    val password: String
)