package com.uifinance.project291.ui.transaction

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

@HiltViewModel
class TransactionViewModel @Inject constructor(
    private val repository: TransactionRepository,
    private val paymentMethodRepository: PaymentMethodRepository
) : ViewModel() {

    private val _amount = MutableStateFlow("")
    val amount: StateFlow<String> = _amount

    private val _selectedCategory = MutableStateFlow<Category?>(null)
    val selectedCategory: StateFlow<Category?> = _selectedCategory

    private val _date = MutableStateFlow(Date())
    val date: StateFlow<Date> = _date

    private val _selectedPaymentMethod = MutableStateFlow<PaymentMethod?>(null)
    val selectedPaymentMethod: StateFlow<PaymentMethod?> = _selectedPaymentMethod

    private val _recurrence = MutableStateFlow(RecurrenceType.NONE)
    val recurrence: StateFlow<RecurrenceType> = _recurrence

    private val _note = MutableStateFlow("")
    val note: StateFlow<String> = _note

    private val _attachmentUri = MutableStateFlow<Uri?>(null)
    val attachmentUri: StateFlow<Uri?> = _attachmentUri

    val paymentMethods: StateFlow<List<PaymentMethod>> = paymentMethodRepository.getAllPaymentMethods()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

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

    fun onCategoryChange(category: Category) {
        _selectedCategory.value = category
    }

    fun onDateChange(date: Date) {
        _date.value = date
    }

    fun onPaymentMethodSelected(paymentMethod: PaymentMethod) {
        _selectedPaymentMethod.value = paymentMethod
    }

    fun onRecurrenceSelected(recurrence: RecurrenceType) {
        _recurrence.value = recurrence
    }

    fun onNoteChange(note: String) {
        _note.value = note
    }

    fun onAttachmentAdded(uri: Uri) {
        _attachmentUri.value = uri
    }

    fun onRemoveAttachment() {
        _attachmentUri.value = null
    }

    fun addCustomPaymentMethod(name: String, icon: String, color: String, type: PaymentMethodType) {
        viewModelScope.launch {
            val newPm = PaymentMethod(name = name, iconRes = icon, colorHex = color, type = type)
            paymentMethodRepository.insertPaymentMethod(newPm)
        }
    }

    fun saveTransaction(type: CategoryType, onSuccess: () -> Unit) {
        val amountValue = _amount.value.toDoubleOrNull() ?: return
        val category = _selectedCategory.value ?: return

        viewModelScope.launch {
            val transaction = TransactionEntity(
                title = category.name,
                subtitle = _note.value.ifBlank { category.name },
                amount = if (type == CategoryType.EXPENSE) -amountValue else amountValue,
                categoryId = category.id.toString(),
                date = _date.value,
                recurrence = _recurrence.value,
                paymentMethodId = _selectedPaymentMethod.value?.id ?: 0,
                attachmentUri = _attachmentUri.value?.toString(),
                note = _note.value,
                type = if (type == CategoryType.EXPENSE) TransactionType.EXPENSE else TransactionType.INCOME
            )
            repository.insertTransaction(transaction)
            onSuccess()
        }
    }
}
