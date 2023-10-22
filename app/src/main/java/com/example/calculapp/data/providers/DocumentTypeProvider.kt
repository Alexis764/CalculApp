package com.example.calculapp.data.providers

import javax.inject.Inject

class DocumentTypeProvider @Inject constructor() {
    fun getDocumentTypes(): Array<String> {
        return arrayOf(
            "Cedula de ciudadanía"
        )
    }
}