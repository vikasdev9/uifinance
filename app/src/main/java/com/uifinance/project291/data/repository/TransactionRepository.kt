package com.uifinance.project291.data.repository

import com.uifinance.project291.data.model.domain.*
import kotlinx.coroutines.flow.Flow

interface TransactionRepository {
    fun getAllTransactions(): Flow<List<Transaction>>
    suspend fun insertTransaction(transaction: Transaction)
    fun getPaymentMethodBalance(id: Long): Flow<Double>
    fun getPaymentMethods(): Flow<List<PaymentMethod>>
    suspend fun addPaymentMethod(paymentMethod: PaymentMethod)
}
