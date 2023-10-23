package com.example.calculapp.domain.documenttype

import com.example.calculapp.R

sealed class DocumentInfo(val type: Int) {
    data object citizenshipCard: DocumentInfo(R.string.citizenshipCard)
}