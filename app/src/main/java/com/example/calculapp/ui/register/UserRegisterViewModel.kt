package com.example.calculapp.ui.register

import androidx.lifecycle.ViewModel
import com.example.calculapp.data.providers.DocumentTypeProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class UserRegisterViewModel @Inject constructor(documentTypeProvider: DocumentTypeProvider): ViewModel() {

    //States
    private var _typeDocumentState = MutableStateFlow<Array<String>>(emptyArray())
    val typeDocumentState: StateFlow<Array<String>> = _typeDocumentState

    init {
        _typeDocumentState.value = documentTypeProvider.getDocumentTypes()
    }

}