package com.example.calculapp.ui.register

import androidx.lifecycle.ViewModel
import com.example.calculapp.data.providers.DocumentTypeProvider
import com.example.calculapp.domain.documenttype.DocumentInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class UserRegisterViewModel @Inject constructor(documentTypeProvider: DocumentTypeProvider): ViewModel() {

    //States
    private var _typeDocumentState = MutableStateFlow<Array<DocumentInfo>>(emptyArray())
    val typeDocumentState: StateFlow<Array<DocumentInfo>> = _typeDocumentState

    init {
        _typeDocumentState.value = documentTypeProvider.getDocumentTypes()
    }

}