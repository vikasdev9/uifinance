package com.uifinance.project291.data.repository

import com.uifinance.project291.data.local.entity.TransactionEntity
import com.uifinance.project291.data.local.entity.TransactionType as DataTransactionType
import com.uifinance.project291.data.local.entity.RecurrenceType as DataRecurrenceType
import com.uifinance.project291.data.model.domain.Transaction
import com.uifinance.project291.data.model.domain.TransactionType
import com.uifinance.project291.data.model.domain.RecurrenceType
import com.uifinance.project291.data.local.entity.PaymentMethod as DataPaymentMethod
import com.uifinance.project291.data.local.entity.PaymentMethodType as DataPaymentMethodType
import com.uifinance.project291.data.model.domain.PaymentMethod
import com.uifinance.project291.data.model.domain.PaymentMethodType

fun Transaction.toEntity(): TransactionEntity = TransactionEntity(
    id = id,
    title = title,
    subtitle = subtitle,
    amount = amount,
    type = when (type) {
        TransactionType.EXPENSE -> DataTransactionType.EXPENSE
        TransactionType.INCOME -> DataTransactionType.INCOME
        TransactionType.TRANSFER -> DataTransactionType.TRANSFER
    },
    categoryId = categoryId,
    date = date,
    recurrence = when (recurrence) {
        RecurrenceType.NONE -> DataRecurrenceType.NONE
        RecurrenceType.DAILY -> DataRecurrenceType.DAILY
        RecurrenceType.WEEKLY -> DataRecurrenceType.WEEKLY
        RecurrenceType.MONTHLY -> DataRecurrenceType.MONTHLY
        RecurrenceType.WEEKEND -> DataRecurrenceType.WEEKEND
    },
    paymentMethodId = paymentMethodId,
    toPaymentMethodId = toPaymentMethodId,
    attachmentUri = attachmentUri,
    note = note
)

fun TransactionEntity.toDomain(): Transaction = Transaction(
    id = id,
    title = title,
    subtitle = subtitle,
    amount = amount,
    type = when (type) {
        DataTransactionType.EXPENSE -> TransactionType.EXPENSE
        DataTransactionType.INCOME -> TransactionType.INCOME
        DataTransactionType.TRANSFER -> TransactionType.TRANSFER
    },
    categoryId = categoryId,
    date = date,
    recurrence = when (recurrence) {
        DataRecurrenceType.NONE -> RecurrenceType.NONE
        DataRecurrenceType.DAILY -> RecurrenceType.DAILY
        DataRecurrenceType.WEEKLY -> RecurrenceType.WEEKLY
        DataRecurrenceType.MONTHLY -> RecurrenceType.MONTHLY
        DataRecurrenceType.WEEKEND -> RecurrenceType.WEEKEND
    },
    paymentMethodId = paymentMethodId,
    toPaymentMethodId = toPaymentMethodId,
    attachmentUri = attachmentUri,
    note = note
)

fun DataPaymentMethod.toDomain(): PaymentMethod = PaymentMethod(
    id = id,
    name = name,
    iconRes = iconRes,
    colorHex = colorHex,
    type = when (type) {
        DataPaymentMethodType.ASSET -> PaymentMethodType.ASSET
        DataPaymentMethodType.LIABILITY -> PaymentMethodType.LIABILITY
    },
    allowNegativeBalance = allowNegativeBalance
)

fun PaymentMethod.toEntity(): DataPaymentMethod = DataPaymentMethod(
    id = id,
    name = name,
    iconRes = iconRes,
    colorHex = colorHex,
    type = when (type) {
        PaymentMethodType.ASSET -> DataPaymentMethodType.ASSET
        PaymentMethodType.LIABILITY -> DataPaymentMethodType.LIABILITY
    },
    allowNegativeBalance = allowNegativeBalance,
    isActive = true
)
