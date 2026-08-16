package com.uifinance.project291.data.model

import androidx.compose.ui.graphics.Color

enum class TransactionType {
    PROPERTY_YIELD,
    GOLD_PURCHASE,
    DIVIDEND,
}

data class Transaction(
    val id: String,
    val title: String,
    val subtitle: String,
    val amount: Double,
    val type: TransactionType,
    val iconTint: Color,
)
