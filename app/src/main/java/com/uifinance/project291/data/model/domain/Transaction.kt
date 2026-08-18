package com.uifinance.project291.data.model.domain

import java.util.Date

enum class TransactionType {
    EXPENSE, INCOME, TRANSFER
}

enum class RecurrenceType {
    NONE, DAILY, WEEKLY, WEEKEND, MONTHLY
}

data class Transaction(
    val id: Long = 0,
    val title: String,
    val subtitle: String,
    val amount: Double,
    val type: TransactionType,
    val categoryId: String,
    val date: Date = Date(),
    val recurrence: RecurrenceType = RecurrenceType.NONE,
    val paymentMethodId: Long = 0,
    val toPaymentMethodId: Long? = null,
    val attachmentUri: String? = null,
    val note: String? = null
)
