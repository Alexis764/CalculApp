package com.example.calculapp.ui.register

import com.example.calculapp.R

sealed class UserRegisterMessageResponse(val message: Int) {
    data object Registered: UserRegisterMessageResponse(R.string.registered)
    data object ErrorIdentification: UserRegisterMessageResponse(R.string.errorIdentification)
    data object ErrorEmail: UserRegisterMessageResponse(R.string.errorEmail)
}