package com.uifinance.project291.data.model

import androidx.compose.ui.graphics.Color

data class AssetPerformance(
    val id: String,
    val name: String,
    val category: AssetCategory,
    val currentValue: Double,
    val changePercentage: Double,
    val accentColor: Color,
)
