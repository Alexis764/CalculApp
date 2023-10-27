package com.example.calculapp.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.calculapp.domain.usecase.GetObjectBadgesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(private val getObjectBadgesUseCase: GetObjectBadgesUseCase): ViewModel(){

    private var _settingsState = MutableStateFlow<SettingsState>(SettingsState.Loading)
    val settingsState: StateFlow<SettingsState> = _settingsState

    fun getBadge(apiKey: String, symbols: String) {
        viewModelScope.launch {
            _settingsState.value = SettingsState.Loading

            val result = withContext(Dispatchers.IO) { getObjectBadgesUseCase(apiKey, symbols) }

            if (result != null) {
                _settingsState.value = SettingsState.Success(result)

            } else {
                _settingsState.value = SettingsState.Error("Ha ocurrido un error, intentelo mas tarde")
            }
        }
    }
}