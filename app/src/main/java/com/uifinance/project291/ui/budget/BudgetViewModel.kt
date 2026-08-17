package com.uifinance.project291.ui.budget

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uifinance.project291.data.local.entity.*
import com.uifinance.project291.data.repository.BudgetRepository
import com.uifinance.project291.data.repository.PaymentMethodRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

sealed class BudgetUiState {
    object Loading : BudgetUiState()
    data class Success(val budgets: List<Budget>) : BudgetUiState()
    data class Error(val message: String) : BudgetUiState()
}

@HiltViewModel
class BudgetViewModel @Inject constructor(
    private val repository: BudgetRepository
) : ViewModel() {

    val uiState: StateFlow<BudgetUiState> = repository.getAllBudgets()
        .map { budgets -> BudgetUiState.Success(budgets) as BudgetUiState }
        .onStart { emit(BudgetUiState.Loading) }
        .catch { e -> emit(BudgetUiState.Error(e.message ?: "Unknown Error")) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), BudgetUiState.Loading)

}
