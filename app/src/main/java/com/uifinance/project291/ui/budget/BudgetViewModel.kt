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

    fun deleteBudget(budget: Budget) {
        viewModelScope.launch {
            repository.deleteBudget(budget)
        }
    }
}

@HiltViewModel
class AddEditBudgetViewModel @Inject constructor(
    private val repository: BudgetRepository,
    private val paymentMethodRepository: PaymentMethodRepository
) : ViewModel() {

    private val _amount = MutableStateFlow("")
    val amount: StateFlow<String> = _amount

    private val _name = MutableStateFlow("")
    val name: StateFlow<String> = _name

    private val _selectedCategory = MutableStateFlow<Category?>(null)
    val selectedCategory: StateFlow<Category?> = _selectedCategory

    private val _period = MutableStateFlow(BudgetPeriod.MONTHLY)
    val period: StateFlow<BudgetPeriod> = _period

    private val _startDate = MutableStateFlow(Date())
    val startDate: StateFlow<Date> = _startDate

    private val _recurrence = MutableStateFlow(RecurrenceType.NONE)
    val recurrence: StateFlow<RecurrenceType> = _recurrence

    private val _alertThreshold = MutableStateFlow(80)
    val alertThreshold: StateFlow<Int> = _alertThreshold

    private val _note = MutableStateFlow("")
    val note: StateFlow<String> = _note

    private val _attachmentUri = MutableStateFlow<Uri?>(null)
    val attachmentUri: StateFlow<Uri?> = _attachmentUri

    private val _selectedPaymentMethodId = MutableStateFlow<Long>(0)
    val selectedPaymentMethodId: StateFlow<Long> = _selectedPaymentMethodId

    init {
        viewModelScope.launch {
            paymentMethodRepository.seedDefaultPaymentMethods()
        }
    }

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
    fun onCategoryChange(category: Category) { _selectedCategory.value = category }
    fun onPeriodChange(period: BudgetPeriod) { _period.value = period }
    fun onStartDateChange(date: Date) { _startDate.value = date }
    fun onRecurrenceChange(recurrence: RecurrenceType) { _recurrence.value = recurrence }
    fun onAlertThresholdChange(threshold: Int) { _alertThreshold.value = threshold }

    fun onNoteChange(note: String) { _note.value = note }

    fun onAttachmentAdded(uri: Uri) { _attachmentUri.value = uri }

    fun onRemoveAttachment() { _attachmentUri.value = null }

    fun onPaymentMethodChange(id: Long) { _selectedPaymentMethodId.value = id }

    fun addCustomPaymentMethod(name: String, icon: String, color: String, type: PaymentMethodType) {
        viewModelScope.launch {
            val newPm = PaymentMethod(name = name, iconRes = icon, colorHex = color, type = type)
            paymentMethodRepository.insertPaymentMethod(newPm)
        }
    }

    fun saveBudget(onSuccess: () -> Unit) {
        val amountLimit = _amount.value.toDoubleOrNull() ?: return
        val category = _selectedCategory.value ?: return
        
        viewModelScope.launch {
            val budget = Budget(
                name = _name.value.ifBlank { "Budget ${category.name}" },
                categoryId = category.id.toString(),
                categoryIcon = category.iconRes,
                amountLimit = amountLimit,
                period = _period.value,
                startDate = _startDate.value,
                endDate = null, // Logic for period end date
                recurrence = _recurrence.value,
                alertThresholdPercent = _alertThreshold.value,
                color = 0xFF10B981.toInt(), // Default color
                note = _note.value,
                paymentMethodId = _selectedPaymentMethodId.value,
                attachmentUri = _attachmentUri.value?.toString()
            )
            repository.insertBudget(budget)
            onSuccess()
        }
    }
}
