package com.uifinance.project291.ui.budget

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Fastfood
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.ShoppingBag
import androidx.compose.material.icons.outlined.DirectionsCar
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uifinance.project291.data.model.Budget
import com.uifinance.project291.data.model.BudgetUiState
import com.uifinance.project291.design_system.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BudgetListViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(BudgetUiState())
    val uiState: StateFlow<BudgetUiState> = _uiState.asStateFlow()

    init {
        loadBudgets()
    }

    private fun loadBudgets() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            delay(500) // Simulate network delay

            val mockBudgets = listOf(
                Budget(
                    id = "1",
                    categoryName = "Housing",
                    categoryIcon = Icons.Outlined.Home,
                    spentAmount = 1850.0,
                    limitAmount = 2000.0,
                    color = EquityBlue,
                    remainingDays = 12
                ),
                Budget(
                    id = "2",
                    categoryName = "Food & Dining",
                    categoryIcon = Icons.Outlined.Fastfood,
                    spentAmount = 420.0,
                    limitAmount = 600.0,
                    color = EmeraldGreen,
                    remainingDays = 12
                ),
                Budget(
                    id = "3",
                    categoryName = "Shopping",
                    categoryIcon = Icons.Outlined.ShoppingBag,
                    spentAmount = 280.0,
                    limitAmount = 300.0,
                    color = MutedGold,
                    remainingDays = 5
                ),
                Budget(
                    id = "4",
                    categoryName = "Transport",
                    categoryIcon = Icons.Outlined.DirectionsCar,
                    spentAmount = 150.0,
                    limitAmount = 200.0,
                    color = CryptoOrange,
                    remainingDays = 12
                )
            )

            _uiState.update {
                it.copy(
                    budgets = mockBudgets,
                    isLoading = false,
                    totalBudgeted = mockBudgets.sumOf { b -> b.limitAmount },
                    totalSpent = mockBudgets.sumOf { b -> b.spentAmount }
                )
            }
        }
    }
}
