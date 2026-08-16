package com.uifinance.project291.ui.payment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uifinance.project291.data.local.entity.PaymentMethod
import com.uifinance.project291.data.local.entity.PaymentMethodType
import com.uifinance.project291.data.repository.PaymentMethodRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class PaymentMethodManagementUiState {
    object Loading : PaymentMethodManagementUiState()
    data class Success(val paymentMethods: List<PaymentMethod>) : PaymentMethodManagementUiState()
    data class Error(val message: String) : PaymentMethodManagementUiState()
}

@HiltViewModel
class PaymentMethodManagementViewModel @Inject constructor(
    private val repository: PaymentMethodRepository
) : ViewModel() {

    val uiState: StateFlow<PaymentMethodManagementUiState> = repository.getAllPaymentMethods()
        .map { methods -> PaymentMethodManagementUiState.Success(methods) as PaymentMethodManagementUiState }
        .onStart { emit(PaymentMethodManagementUiState.Loading) }
        .catch { e -> emit(PaymentMethodManagementUiState.Error(e.message ?: "Unknown error")) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = PaymentMethodManagementUiState.Loading
        )

    fun addPaymentMethod(name: String, iconRes: String, colorHex: String, type: PaymentMethodType) {
        viewModelScope.launch {
            if (isDuplicateName(name)) return@launch
            val pm = PaymentMethod(name = name, iconRes = iconRes, colorHex = colorHex, type = type)
            repository.insertPaymentMethod(pm)
        }
    }

    private fun isDuplicateName(name: String): Boolean {
        val state = uiState.value
        return if (state is PaymentMethodManagementUiState.Success) {
            state.paymentMethods.any { it.name.equals(name, ignoreCase = true) }
        } else false
    }

    fun updatePaymentMethod(paymentMethod: PaymentMethod) {
        viewModelScope.launch {
            repository.updatePaymentMethod(paymentMethod)
        }
    }

    fun deletePaymentMethod(paymentMethod: PaymentMethod) {
        viewModelScope.launch {
            repository.softDeletePaymentMethod(paymentMethod)
        }
    }
}
