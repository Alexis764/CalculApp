package com.example.calculapp.domain.user

import com.example.calculapp.domain.documenttype.DocumentInfo

data class UserModel (
    val name: String,
    val lastName: String,
    val documentType: DocumentInfo
)