package com.uifinance.project291.ui.analytics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uifinance.project291.data.MockData
import com.uifinance.project291.data.model.AnalyticsUiState
import com.uifinance.project291.data.model.TimeRange
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AnalyticsViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(AnalyticsUiState())
    val uiState: StateFlow<AnalyticsUiState> = _uiState.asStateFlow()

    init {
        loadAnalytics()
    }

    fun onTimeRangeSelected(timeRange: TimeRange) {
        _uiState.update { current ->
            MockData.analyticsUiState(timeRange)
        }
    }

    private fun loadAnalytics() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            delay(300)
            _uiState.value = MockData.analyticsUiState()
        }
    }
}
