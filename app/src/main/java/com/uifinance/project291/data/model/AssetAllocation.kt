package com.uifinance.project291.data.model

import androidx.compose.ui.graphics.Color

data class AssetAllocation(
    val id: String,
    val name: String,
    val percentage: Float,
    val color: Color,
)
