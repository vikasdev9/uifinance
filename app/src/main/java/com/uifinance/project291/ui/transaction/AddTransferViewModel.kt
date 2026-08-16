package com.uifinance.project291.ui.transaction

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uifinance.project291.data.local.entity.PaymentMethod
import com.uifinance.project291.data.local.entity.RecurrenceType
import com.uifinance.project291.data.local.entity.TransactionEntity
import com.uifinance.project291.data.local.entity.TransactionType
import com.uifinance.project291.data.repository.PaymentMethodRepository
import com.uifinance.project291.data.repository.TransactionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi

data class AddTransferUiState(
    val amount: String = "",
    val fromWallet: PaymentMethod? = null,
    val toWallet: PaymentMethod? = null,
    val fromBalance: Double = 0.0,
    val toBalance: Double = 0.0,
    val date: Date = Date(),
    val recurrence: RecurrenceType = RecurrenceType.NONE,
    val note: String = "",
    val attachmentUri: String? = null,
    val isSaving: Boolean = false
) {
    val amountValue: Double get() = amount.toDoubleOrNull() ?: 0.0
    val newFromBalance: Double get() = fromBalance - amountValue
    val newToBalance: Double get() = toBalance + amountValue
    
    val isInsufficientBalance: Boolean 
        get() = fromWallet != null && !fromWallet.allowNegativeBalance && amountValue > fromBalance

    val isValid: Boolean 
        get() = amountValue > 0 && 
                fromWallet != null && 
                toWallet != null && 
                fromWallet.id != toWallet.id && 
                !isInsufficientBalance
}

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class AddTransferViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository,
    private val paymentMethodRepository: PaymentMethodRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddTransferUiState())
    val uiState: StateFlow<AddTransferUiState> = _uiState.asStateFlow()

    val paymentMethods: StateFlow<List<PaymentMethod>> = paymentMethodRepository.getAllPaymentMethods()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    init {
        // Observe balance for 'from' wallet
        _uiState.map { it.fromWallet?.id }
            .distinctUntilChanged()
            .flatMapLatest { id ->
                if (id != null) transactionRepository.getPaymentMethodBalance(id)
                else flowOf(0.0)
            }
            .onEach { balance -> _uiState.update { it.copy(fromBalance = balance) } }
            .launchIn(viewModelScope)

        // Observe balance for 'to' wallet
        _uiState.map { it.toWallet?.id }
            .distinctUntilChanged()
            .flatMapLatest { id ->
                if (id != null) transactionRepository.getPaymentMethodBalance(id)
                else flowOf(0.0)
            }
            .onEach { balance -> _uiState.update { it.copy(toBalance = balance) } }
            .launchIn(viewModelScope)
    }

    fun onAmountChange(value: String) {
        _uiState.update { state ->
            val currentAmount = state.amount
            if (value == "." && currentAmount.contains(".")) return@update state
            state.copy(amount = currentAmount + value)
        }
    }

    fun onAmountDelete() {
        _uiState.update { state ->
            if (state.amount.isNotEmpty()) {
                state.copy(amount = state.amount.dropLast(1))
            } else state
        }
    }

    fun onFromWalletSelected(wallet: PaymentMethod) {
        _uiState.update { it.copy(fromWallet = wallet) }
        viewModelScope.launch {
            transactionRepository.getPaymentMethodBalance(wallet.id).firstOrNull()?.let { balance ->
                _uiState.update { it.copy(fromBalance = balance) }
            }
        }
    }

    fun onToWalletSelected(wallet: PaymentMethod) {
        _uiState.update { it.copy(toWallet = wallet) }
        viewModelScope.launch {
            transactionRepository.getPaymentMethodBalance(wallet.id).firstOrNull()?.let { balance ->
                _uiState.update { it.copy(toBalance = balance) }
            }
        }
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

    fun onDateChange(date: Date) {
        _uiState.update { it.copy(date = date) }
    }

    fun onRecurrenceSelected(recurrence: RecurrenceType) {
        _uiState.update { it.copy(recurrence = recurrence) }
    }

    fun onNoteChange(note: String) {
        _uiState.update { it.copy(note = note) }
    }

    fun onAttachmentAdded(uri: android.net.Uri) {
        _uiState.update { it.copy(attachmentUri = uri.toString()) }
    }

    fun onRemoveAttachment() {
        _uiState.update { it.copy(attachmentUri = null) }
    }

    fun saveTransfer(onSuccess: () -> Unit) {
        val state = _uiState.value
        if (!state.isValid) return

        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true) }
            val transfer = TransactionEntity(
                title = "Transfer",
                subtitle = "From ${state.fromWallet?.name} to ${state.toWallet?.name}",
                amount = state.amountValue,
                type = TransactionType.TRANSFER,
                categoryId = "transfer", // Dedicated or default category
                date = state.date,
                recurrence = state.recurrence,
                paymentMethodId = state.fromWallet?.id ?: 0,
                toPaymentMethodId = state.toWallet?.id,
                note = state.note,
                attachmentUri = state.attachmentUri
            )
            transactionRepository.insertTransaction(transfer)
            _uiState.update { it.copy(isSaving = false) }
            onSuccess()
        }
    }
}
