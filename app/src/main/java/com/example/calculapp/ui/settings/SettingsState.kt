package com.example.calculapp.ui.settings

import com.example.calculapp.domain.model.BadgeModel

sealed class SettingsState {
    data object Loading: SettingsState()
    data class Error(val error: String): SettingsState()
    data class Success(val badgeModel: BadgeModel): SettingsState()
}