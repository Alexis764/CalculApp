package com.example.calculapp.ui.home.adapter

import com.example.calculapp.data.sqlite.model.CreditModel

interface HomeListeners {
    fun onButtonDetailCLick(creditModel: CreditModel)
}