package com.uifinance.project291.data.repository

import com.uifinance.project291.data.local.dao.TransactionDao
import com.uifinance.project291.data.local.entity.TransactionEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TransactionRepository @Inject constructor(
    private val transactionDao: TransactionDao
) {
    fun getAllTransactions(): Flow<List<TransactionEntity>> = transactionDao.getAllTransactions()

    suspend fun insertTransaction(transaction: TransactionEntity) = transactionDao.insertTransaction(transaction)

    fun getPaymentMethodBalance(id: Long): Flow<Double> = transactionDao.getPaymentMethodBalance(id)
}
