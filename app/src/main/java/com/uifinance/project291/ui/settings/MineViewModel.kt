package com.uifinance.project291.ui.settings

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class MineViewModel @Inject constructor() : ViewModel() {

    private val _theme = MutableStateFlow("Dark Luxury")
    val theme: StateFlow<String> = _theme.asStateFlow()

    private val _accentColor = MutableStateFlow("Emerald Green")
    val accentColor: StateFlow<String> = _accentColor.asStateFlow()

    private val _remindersEnabled = MutableStateFlow(true)
    val remindersEnabled: StateFlow<Boolean> = _remindersEnabled.asStateFlow()

    fun onThemeChange(newTheme: String) {
        _theme.value = newTheme
    }

    fun onAccentColorChange(newColor: String) {
        _accentColor.value = newColor
    }

    fun toggleReminders(enabled: Boolean) {
        _remindersEnabled.value = enabled
    }
}
