package com.uifinance.project291.data.model

import androidx.compose.ui.graphics.Color

data class Budget(
    val id: String,
    val categoryName: String,
    val categoryIcon: androidx.compose.ui.graphics.vector.ImageVector,
    val spentAmount: Double,
    val limitAmount: Double,
    val color: Color,
    val remainingDays: Int
)

data class BudgetUiState(
    val budgets: List<Budget> = emptyList(),
    val isLoading: Boolean = false,
    val totalBudgeted: Double = 0.0,
    val totalSpent: Double = 0.0
)
