package com.example.calculapp.ui.credit

import com.example.calculapp.R

sealed class CreditRegisterMessageResponse(val message: Int) {
    data object CreditRegistered: CreditRegisterMessageResponse(R.string.creditRegistered)
    data object RequestInProcess: CreditRegisterMessageResponse(R.string.requestInProcess)
    data object CurrentCredit: CreditRegisterMessageResponse(R.string.currentCredit)
}