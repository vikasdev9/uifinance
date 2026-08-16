package com.uifinance.project291.data.model

data class AnalyticsUiState(
    val totalPortfolioValue: Double = 0.0,
    val growthPercentage: Double = 0.0,
    val selectedTimeRange: TimeRange = TimeRange.ONE_YEAR,
    val chartPoints: List<ChartDataPoint> = emptyList(),
    val monthLabels: List<String> = emptyList(),
    val topAssets: List<AssetPerformance> = emptyList(),
    val isLoading: Boolean = true,
)
