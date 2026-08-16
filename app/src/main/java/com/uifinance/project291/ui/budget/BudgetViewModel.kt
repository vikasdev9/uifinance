package com.uifinance.project291.ui.budget

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uifinance.project291.data.local.entity.Budget
import com.uifinance.project291.data.local.entity.BudgetPeriod
import com.uifinance.project291.data.local.entity.Recurrence
import com.uifinance.project291.data.repository.BudgetRepository
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

    fun deleteBudget(budget: Budget) {
        viewModelScope.launch {
            repository.deleteBudget(budget)
        }
    }
}

@HiltViewModel
class AddEditBudgetViewModel @Inject constructor(
    private val repository: BudgetRepository
) : ViewModel() {

    private val _amount = MutableStateFlow("")
    val amount: StateFlow<String> = _amount

    private val _name = MutableStateFlow("")
    val name: StateFlow<String> = _name

    private val _categoryId = MutableStateFlow<String?>(null)
    val categoryId: StateFlow<String?> = _categoryId

    private val _period = MutableStateFlow(BudgetPeriod.MONTHLY)
    val period: StateFlow<BudgetPeriod> = _period

    private val _startDate = MutableStateFlow(Date())
    val startDate: StateFlow<Date> = _startDate

    private val _recurrence = MutableStateFlow(Recurrence.MONTHLY)
    val recurrence: StateFlow<Recurrence> = _recurrence

    private val _alertThreshold = MutableStateFlow(80)
    val alertThreshold: StateFlow<Int> = _alertThreshold

    fun onAmountChange(value: String) {
        if (value == "." && _amount.value.contains(".")) return
        _amount.value += value
    }

    fun onAmountDelete() {
        if (_amount.value.isNotEmpty()) {
            _amount.value = _amount.value.dropLast(1)
        }
    }

    fun onNameChange(value: String) { _name.value = value }
    fun onCategoryChange(id: String) { _categoryId.value = id }
    fun onPeriodChange(period: BudgetPeriod) { _period.value = period }
    fun onStartDateChange(date: Date) { _startDate.value = date }
    fun onRecurrenceChange(recurrence: Recurrence) { _recurrence.value = recurrence }
    fun onAlertThresholdChange(threshold: Int) { _alertThreshold.value = threshold }

    fun saveBudget(onSuccess: () -> Unit) {
        val amountLimit = _amount.value.toDoubleOrNull() ?: return
        val catId = _categoryId.value ?: return
        
        viewModelScope.launch {
            val budget = Budget(
                name = _name.value.ifBlank { "Budget $catId" },
                categoryId = catId,
                categoryIcon = 0, // Should be mapped from category
                amountLimit = amountLimit,
                period = _period.value,
                startDate = _startDate.value,
                endDate = null, // Logic for period end date
                recurrence = _recurrence.value,
                alertThresholdPercent = _alertThreshold.value,
                color = 0xFF10B981.toInt() // Default color
            )
            repository.insertBudget(budget)
            onSuccess()
        }
    }
}
