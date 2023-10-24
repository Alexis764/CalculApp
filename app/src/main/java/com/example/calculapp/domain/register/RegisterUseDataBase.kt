package com.example.calculapp.domain.register

import android.content.Context
import com.example.calculapp.data.sqlite.UserCreditsDataBase
import com.example.calculapp.domain.user.UserModel
import com.example.calculapp.ui.register.UserRegisterMessageResponse

class RegisterUseDataBase(context: Context) {

    //Val and var
    private val userCreditsDataBase = UserCreditsDataBase(context)


    fun registerUser(
        firstName: String,
        secondName: String,
        lastName: String,
        secondLastName: String,
        documentType: String,
        identificationNumber: String,
        email: String,
        phoneNumber: String,
        password: String
    ): UserRegisterMessageResponse {
        val name = if (secondName.isNotEmpty()) {"$firstName $secondName"} else firstName
        val lastNameComplete = if (secondLastName.isNotEmpty()) {"$lastName $secondLastName"} else lastName
        val userModel = UserModel(
            name,
            lastNameComplete,
            documentType,
            identificationNumber.toLong(),
            email,
            phoneNumber.toLong(),
            password
        )

        return userCreditsDataBase.insertUser(userModel)
    }


    fun registerFirstCredit(valueMoney: Int, valueDays: Int, userIdentificationNumber: Long): Int {
        return userCreditsDataBase.insertCredit(valueMoney, valueDays, userIdentificationNumber).message
    }

}