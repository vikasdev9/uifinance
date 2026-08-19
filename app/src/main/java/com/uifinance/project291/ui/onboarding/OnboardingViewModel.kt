package com.uifinance.project291.ui.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uifinance.project291.financeUtils.AppPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val appPreferences: AppPreferences
) : ViewModel() {

    fun completeOnboarding() {
        viewModelScope.launch {
            appPreferences.setOnboardingCompleted(true)
        }
    }
}
