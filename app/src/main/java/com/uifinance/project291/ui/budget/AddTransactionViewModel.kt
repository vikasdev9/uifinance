package com.uifinance.project291.ui.budget

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uifinance.project291.data.model.domain.*
import com.uifinance.project291.data.usecase.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi

data class AddTransactionUiState(
    val amount: String = "",
    val selectedTab: Int = 0,
    val selectedCategory: Category? = null,
    val selectedPaymentMethod: PaymentMethod? = null,
    val fromWallet: PaymentMethod? = null,
    val toWallet: PaymentMethod? = null,
    val fromBalance: Double = 0.0,
    val toBalance: Double = 0.0,
    val date: Date = Date(),
    val recurrence: RecurrenceType = RecurrenceType.NONE,
    val note: String = "",
    val attachmentUri: Uri? = null,
    val isSaving: Boolean = false,
    val paymentMethods: List<PaymentMethod> = emptyList()
) {
    val amountValue: Double get() = amount.toDoubleOrNull() ?: 0.0
    val newFromBalance: Double get() = fromBalance - amountValue
    val newToBalance: Double get() = toBalance + amountValue
    
    val isInsufficientBalance: Boolean 
        get() = selectedTab == 2 && fromWallet != null && !fromWallet.allowNegativeBalance && amountValue > fromBalance

    val isValid: Boolean 
        get() = when (selectedTab) {
            2 -> amountValue > 0 && fromWallet != null && toWallet != null && fromWallet.id != toWallet.id && !isInsufficientBalance
            else -> amountValue > 0 && selectedCategory != null && selectedPaymentMethod != null
        }
}

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class AddTransactionViewModel @Inject constructor(
    private val addTransactionUseCase: AddTransactionUseCase,
    private val getPaymentMethodsUseCase: GetPaymentMethodsUseCase,
    private val getPaymentMethodBalanceUseCase: GetPaymentMethodBalanceUseCase,
    private val seedPaymentMethodsUseCase: SeedPaymentMethodsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddTransactionUiState())
    val uiState: StateFlow<AddTransactionUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            seedPaymentMethodsUseCase()
        }

        getPaymentMethodsUseCase().onEach { methods ->
            _uiState.update { it.copy(paymentMethods = methods) }
        }.launchIn(viewModelScope)

        // Observe balances for Transfer mode
        _uiState.map { it.fromWallet?.id }
            .distinctUntilChanged()
            .flatMapLatest { id ->
                if (id != null) getPaymentMethodBalanceUseCase(id)
                else flowOf(0.0)
            }
            .onEach { balance -> _uiState.update { it.copy(fromBalance = balance) } }
            .launchIn(viewModelScope)

        _uiState.map { it.toWallet?.id }
            .distinctUntilChanged()
            .flatMapLatest { id ->
                if (id != null) getPaymentMethodBalanceUseCase(id)
                else flowOf(0.0)
            }
            .onEach { balance -> _uiState.update { it.copy(toBalance = balance) } }
            .launchIn(viewModelScope)
    }

    fun onTabSelected(index: Int) {
        _uiState.update { it.copy(selectedTab = index) }
    }

    fun onAmountChange(value: String) {
        _uiState.update { state ->
            if (value == "." && state.amount.contains(".")) return@update state
            state.copy(amount = state.amount + value)
        }
    }

    fun onAmountDelete() {
        _uiState.update { state ->
            if (state.amount.isNotEmpty()) {
                state.copy(amount = state.amount.dropLast(1))
            } else state
        }
    }

    fun onCategoryChange(category: Category) {
        _uiState.update { it.copy(selectedCategory = category) }
    }

    fun onDateChange(date: Date) {
        _uiState.update { it.copy(date = date) }
    }

    fun onPaymentMethodSelected(paymentMethod: PaymentMethod) {
        _uiState.update { it.copy(selectedPaymentMethod = paymentMethod) }
    }

    fun onFromWalletSelected(wallet: PaymentMethod) {
        _uiState.update { it.copy(fromWallet = wallet) }
    }

    fun onToWalletSelected(wallet: PaymentMethod) {
        _uiState.update { it.copy(toWallet = wallet) }
    }

    fun onSwapWallets() {
        _uiState.update { state ->
            state.copy(
                fromWallet = state.toWallet,
                toWallet = state.fromWallet,
                fromBalance = state.toBalance,
                toBalance = state.fromBalance
            )
        }
    }

    fun onRecurrenceSelected(recurrence: RecurrenceType) {
        _uiState.update { it.copy(recurrence = recurrence) }
    }

    fun onNoteChange(note: String) {
        _uiState.update { it.copy(note = note) }
    }

    fun onAttachmentAdded(uri: Uri) {
        _uiState.update { it.copy(attachmentUri = uri) }
    }

    fun onRemoveAttachment() {
        _uiState.update { it.copy(attachmentUri = null) }
    }

    fun saveTransaction(onSuccess: () -> Unit) {
        val state = _uiState.value
        if (!state.isValid) return

        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true) }
            
            val transaction = when (state.selectedTab) {
                0 -> Transaction(
                    title = state.selectedCategory?.name ?: "",
                    subtitle = state.note.ifBlank { state.selectedCategory?.name ?: "" },
                    amount = -state.amountValue,
                    type = TransactionType.EXPENSE,
                    categoryId = state.selectedCategory?.id.toString(),
                    date = state.date,
                    recurrence = state.recurrence,
                    paymentMethodId = state.selectedPaymentMethod?.id ?: 0,
                    attachmentUri = state.attachmentUri?.toString(),
                    note = state.note
                )
                1 -> Transaction(
                    title = state.selectedCategory?.name ?: "",
                    subtitle = state.note.ifBlank { state.selectedCategory?.name ?: "" },
                    amount = state.amountValue,
                    type = TransactionType.INCOME,
                    categoryId = state.selectedCategory?.id.toString(),
                    date = state.date,
                    recurrence = state.recurrence,
                    paymentMethodId = state.selectedPaymentMethod?.id ?: 0,
                    attachmentUri = state.attachmentUri?.toString(),
                    note = state.note
                )
                2 -> Transaction(
                    title = "Transfer",
                    subtitle = "From ${state.fromWallet?.name} to ${state.toWallet?.name}",
                    amount = state.amountValue,
                    type = TransactionType.TRANSFER,
                    categoryId = "transfer",
                    date = state.date,
                    recurrence = state.recurrence,
                    paymentMethodId = state.fromWallet?.id ?: 0,
                    toPaymentMethodId = state.toWallet?.id,
                    attachmentUri = state.attachmentUri?.toString(),
                    note = state.note
                )
                else -> throw IllegalStateException("Invalid tab index")
            }

            addTransactionUseCase(transaction)
            _uiState.update { it.copy(isSaving = false) }
            onSuccess()
        }
    }
}
