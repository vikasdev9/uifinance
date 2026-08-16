package com.uifinance.project291.ui.budget

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uifinance.project291.data.local.entity.*
import com.uifinance.project291.data.repository.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

enum class BudgetEntryType {
    BUDGET, INCOME, EXPENSE
}

data class AddBudgetUiState(
    val amount: String = "",
    val name: String = "",
    val entryType: BudgetEntryType = BudgetEntryType.BUDGET,
    val selectedCategory: Category? = null,
    val selectedPaymentMethod: PaymentMethod? = null,
    val date: Date = Date(),
    val recurrence: RecurrenceType = RecurrenceType.NONE,
    val note: String = "",
    val attachmentUri: Uri? = null,
    val alertThresholdPercent: Int = 80,
    val isKeypadVisible: Boolean = true,
    val isSaving: Boolean = false,
    val saveSuccess: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class AddBudgetViewModel @Inject constructor(
    private val budgetRepository: BudgetRepository,
    private val categoryRepository: CategoryRepository,
    private val paymentMethodRepository: PaymentMethodRepository,
    private val transactionRepository: TransactionRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddBudgetUiState())
    val uiState: StateFlow<AddBudgetUiState> = _uiState.asStateFlow()

    val paymentMethods: StateFlow<List<PaymentMethod>> = paymentMethodRepository.getAllPaymentMethods()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        viewModelScope.launch {
            paymentMethodRepository.seedDefaultPaymentMethods()
        }
    }

    fun onAmountChange(digit: String) {
        val current = _uiState.value.amount
        if (digit == "." && current.contains(".")) return
        _uiState.update { it.copy(amount = current + digit) }
    }

    fun onAmountDelete() {
        val current = _uiState.value.amount
        if (current.isNotEmpty()) {
            _uiState.update { it.copy(amount = current.dropLast(1)) }
        }
    }

    fun onEntryTypeChange(type: BudgetEntryType) {
        _uiState.update { it.copy(entryType = type, selectedCategory = null) }
    }

    fun onToggleSign() {
        val current = _uiState.value.amount
        if (current.isEmpty()) return
        val newValue = if (current.startsWith("-")) current.drop(1) else "-$current"
        _uiState.update { it.copy(amount = newValue) }
    }

    fun onNameChange(name: String) {
        _uiState.update { it.copy(name = name) }
    }

    fun onCategorySelected(category: Category) {
        _uiState.update { it.copy(selectedCategory = category) }
    }

    fun onPaymentMethodSelected(paymentMethod: PaymentMethod) {
        _uiState.update { it.copy(selectedPaymentMethod = paymentMethod) }
    }

    fun onDateSelected(date: Date) {
        _uiState.update { it.copy(date = date) }
    }

    fun onRecurrenceSelected(recurrence: RecurrenceType) {
        _uiState.update { it.copy(recurrence = recurrence) }
    }

    fun onNoteChange(note: String) {
        _uiState.update { it.copy(note = note) }
    }

    fun onAlertThresholdChange(percent: Int) {
        _uiState.update { it.copy(alertThresholdPercent = percent) }
    }

    fun onAttachmentAdded(uri: Uri) {
        _uiState.update { it.copy(attachmentUri = uri) }
    }

    fun onRemoveAttachment() {
        _uiState.update { it.copy(attachmentUri = null) }
    }

    fun toggleKeypad() {
        _uiState.update { it.copy(isKeypadVisible = !it.isKeypadVisible) }
    }

    fun saveBudget() {
        val state = _uiState.value
        val amount = state.amount.toDoubleOrNull() ?: 0.0
        
        if (amount <= 0 || state.selectedCategory == null) {
            _uiState.update { it.copy(error = "Please enter amount and select category") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true) }
            try {
                when (state.entryType) {
                    BudgetEntryType.BUDGET -> {
                        val budget = Budget(
                            name = state.name.ifEmpty { state.selectedCategory.name },
                            categoryId = state.selectedCategory.id.toString(),
                            categoryIcon = state.selectedCategory.iconRes,
                            amountLimit = amount,
                            period = BudgetPeriod.MONTHLY,
                            startDate = state.date,
                            endDate = null,
                            recurrence = state.recurrence,
                            alertThresholdPercent = state.alertThresholdPercent,
                            color = android.graphics.Color.parseColor(state.selectedCategory.colorHex),
                            note = state.note,
                            paymentMethodId = state.selectedPaymentMethod?.id ?: 0,
                            attachmentUri = state.attachmentUri?.toString()
                        )
                        budgetRepository.insertBudget(budget)
                    }
                    BudgetEntryType.INCOME, BudgetEntryType.EXPENSE -> {
                        val transaction = TransactionEntity(
                            title = state.selectedCategory.name,
                            subtitle = state.note.ifEmpty { state.selectedCategory.name },
                            amount = amount,
                            type = if (state.entryType == BudgetEntryType.INCOME) TransactionType.INCOME else TransactionType.EXPENSE,
                            categoryId = state.selectedCategory.id.toString(),
                            date = state.date,
                            recurrence = state.recurrence,
                            paymentMethodId = state.selectedPaymentMethod?.id ?: 0,
                            attachmentUri = state.attachmentUri?.toString(),
                            note = state.note
                        )
                        transactionRepository.insertTransaction(transaction)
                    }
                }
                _uiState.update { it.copy(isSaving = false, saveSuccess = true) }
            } catch (e: Exception) {
                _uiState.update { it.copy(isSaving = false, error = e.message) }
            }
        }
    }
    
    fun addCustomPaymentMethod(name: String, icon: String, color: String, type: PaymentMethodType) {
        viewModelScope.launch {
            val newPm = PaymentMethod(name = name, iconRes = icon, colorHex = color, type = type)
            paymentMethodRepository.insertPaymentMethod(newPm)
        }
    }
}
