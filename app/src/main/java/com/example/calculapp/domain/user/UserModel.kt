package com.example.calculapp.domain.user

data class UserModel (
    val name: String,
    val lastName: String,
    val documentType: String,
    val identificationNumber: Int,
    val email: String,
    val phoneNumber: Int,
    val password: String
)