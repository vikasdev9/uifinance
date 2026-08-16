package com.uifinance.project291.data.model

data class DashboardUiState(
    val profileImageUrl: String = "",
    val totalNetWorth: Double = 0.0,
    val growthPercentage: Double = 0.0,
    val assetCount: Int = 0,
    val allocations: List<AssetAllocation> = emptyList(),
    val recentTransactions: List<Transaction> = emptyList(),
    val isLoading: Boolean = true,
)
