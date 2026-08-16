package com.uifinance.project291.data

import com.uifinance.project291.data.model.AnalyticsUiState
import com.uifinance.project291.data.model.AssetAllocation
import com.uifinance.project291.data.model.AssetCategory
import com.uifinance.project291.data.model.AssetPerformance
import com.uifinance.project291.data.model.ChartDataPoint
import com.uifinance.project291.data.model.DashboardUiState
import com.uifinance.project291.data.model.TimeRange
import com.uifinance.project291.data.model.Transaction
import com.uifinance.project291.data.model.TransactionType
import com.uifinance.project291.design_system.CryptoOrange
import com.uifinance.project291.design_system.EquityBlue
import com.uifinance.project291.design_system.EmeraldGreen
import com.uifinance.project291.design_system.MutedGold
import com.uifinance.project291.design_system.RealEstateGreen

object MockData {

    private val allocations = listOf(
        AssetAllocation(
            id = "gold",
            name = "Gold",
            percentage = 45f,
            color = MutedGold,
        ),
        AssetAllocation(
            id = "real_estate",
            name = "Real Estate",
            percentage = 35f,
            color = RealEstateGreen,
        ),
        AssetAllocation(
            id = "cash",
            name = "Cash",
            percentage = 20f,
            color = EmeraldGreen,
        ),
    )

    private val recentTransactions = listOf(
        Transaction(
            id = "tx_1",
            title = "Property Yield",
            subtitle = "London Estate • Today",
            amount = 12_450.00,
            type = TransactionType.PROPERTY_YIELD,
            iconTint = EmeraldGreen,
        ),
        Transaction(
            id = "tx_2",
            title = "Gold Purchase",
            subtitle = "Vault Swiss • Yesterday",
            amount = -45_000.00,
            type = TransactionType.GOLD_PURCHASE,
            iconTint = MutedGold,
        ),
        Transaction(
            id = "tx_3",
            title = "Dividend",
            subtitle = "Tech Fund • Mar 12",
            amount = 3_200.00,
            type = TransactionType.DIVIDEND,
            iconTint = EmeraldGreen,
        ),
    )

    val dashboardUiState = DashboardUiState(
        profileImageUrl = "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?auto=format&fit=crop&w=200&q=80",
        totalNetWorth = 1_284_500.00,
        growthPercentage = 8.4,
        assetCount = 3,
        allocations = allocations,
        recentTransactions = recentTransactions,
        isLoading = false,
    )

    private val oneMonthChart = listOf(
        ChartDataPoint(0.32f),
        ChartDataPoint(0.58f),
        ChartDataPoint(0.75f),
        ChartDataPoint(0.62f),
        ChartDataPoint(0.38f),
        ChartDataPoint(0.91f),
        ChartDataPoint(0.38f),
        ChartDataPoint(0.65f),
        ChartDataPoint(0.72f),
        ChartDataPoint(0.80f),
    )

    private val sixMonthChart = listOf(
        ChartDataPoint(0.35f),
        ChartDataPoint(0.42f),
        ChartDataPoint(0.58f),
        ChartDataPoint(0.35f),
        ChartDataPoint(0.34f),
        ChartDataPoint(0.82f),
        ChartDataPoint(0.58f),
        ChartDataPoint(0.93f),
        ChartDataPoint(0.58f),
        ChartDataPoint(0.35f),
        ChartDataPoint(0.94f),
    )

    private val oneYearChart = listOf(
        ChartDataPoint(0.18f),
        ChartDataPoint(0.24f),
        ChartDataPoint(0.38f),
        ChartDataPoint(0.58f),
        ChartDataPoint(0.72f),
        ChartDataPoint(0.52f),
        ChartDataPoint(0.38f),
        ChartDataPoint(0.65f),
        ChartDataPoint(0.72f),
        ChartDataPoint(0.50f),
        ChartDataPoint(0.38f),
        ChartDataPoint(0.95f),
    )

    private val allTimeChart = listOf(

        ChartDataPoint(0.65f),
        ChartDataPoint(0.72f),
        ChartDataPoint(0.50f),
        ChartDataPoint(0.85f),
        ChartDataPoint(0.34f),
        ChartDataPoint(0.38f),
        ChartDataPoint(0.58f),
        ChartDataPoint(0.72f),
        ChartDataPoint(0.52f),
        ChartDataPoint(0.38f),
        ChartDataPoint(0.65f),
        ChartDataPoint(0.72f),
        ChartDataPoint(0.60f),
        ChartDataPoint(0.78f),
        ChartDataPoint(0.95f),

    )

    private val topPerformingAssets = listOf(
        AssetPerformance(
            id = "bitcoin_vault",
            name = "Bitcoin Vault",
            category = AssetCategory.CRYPTO,
            currentValue = 1_240_500.0,
            changePercentage = 45.2,
            accentColor = CryptoOrange,
        ),
        AssetPerformance(
            id = "tech_index",
            name = "Tech Index Fund",
            category = AssetCategory.EQUITY,
            currentValue = 850_200.0,
            changePercentage = 18.7,
            accentColor = EquityBlue,
        ),
        AssetPerformance(
            id = "real_estate_trust",
            name = "Prime Real Estate Trust",
            category = AssetCategory.REAL_ASSET,
            currentValue = 2_100_000.0,
            changePercentage = 8.4,
            accentColor = MutedGold,
        ),
    )

    fun chartPointsFor(timeRange: TimeRange): List<ChartDataPoint> = when (timeRange) {
        TimeRange.ONE_MONTH -> oneMonthChart
        TimeRange.SIX_MONTHS -> sixMonthChart
        TimeRange.ONE_YEAR -> oneYearChart
        TimeRange.ALL -> allTimeChart
    }

    fun monthLabelsFor(timeRange: TimeRange): List<String> = when (timeRange) {
        TimeRange.ONE_MONTH -> listOf("W1", "W2", "W3", "W4")
        TimeRange.SIX_MONTHS -> listOf("Jul", "Aug", "Sep", "Oct", "Nov", "Dec")
        TimeRange.ONE_YEAR -> listOf("Jan", "Apr", "Jul", "Oct", "Dec")
        TimeRange.ALL -> listOf("Y1", "Y2", "Y3", "Y4", "Y5")
    }

    fun analyticsUiState(selectedTimeRange: TimeRange = TimeRange.ONE_YEAR) = AnalyticsUiState(
        totalPortfolioValue = 4_285_100.00,
        growthPercentage = 12.4,
        selectedTimeRange = selectedTimeRange,
        chartPoints = chartPointsFor(selectedTimeRange),
        monthLabels = monthLabelsFor(selectedTimeRange),
        topAssets = topPerformingAssets,
        isLoading = false,
    )
}
