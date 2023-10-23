package com.example.calculapp.data.providers

import com.example.calculapp.domain.documenttype.DocumentInfo
import com.example.calculapp.domain.documenttype.DocumentInfo.*
import javax.inject.Inject

class DocumentTypeProvider @Inject constructor() {
    fun getDocumentTypes(): Array<DocumentInfo> {
        return arrayOf(
            citizenshipCard
        )
    }
}