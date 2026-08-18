package com.uifinance.project291.data.repository

import com.uifinance.project291.data.local.dao.TransactionDao
import com.uifinance.project291.data.local.dao.PaymentMethodDao
import com.uifinance.project291.data.model.domain.PaymentMethod
import com.uifinance.project291.data.model.domain.Transaction
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TransactionRepositoryImpl @Inject constructor(
    private val transactionDao: TransactionDao,
    private val paymentMethodDao: PaymentMethodDao
) : TransactionRepository {

    override fun getAllTransactions(): Flow<List<Transaction>> {
        return transactionDao.getAllTransactions().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun insertTransaction(transaction: Transaction) {
        transactionDao.insertTransaction(transaction.toEntity())
    }

    override fun getPaymentMethodBalance(id: Long): Flow<Double> {
        return transactionDao.getPaymentMethodBalance(id)
    }

    override fun getPaymentMethods(): Flow<List<PaymentMethod>> {
        return paymentMethodDao.getAllPaymentMethods().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun addPaymentMethod(paymentMethod: PaymentMethod) {
        paymentMethodDao.insertPaymentMethod(paymentMethod.toEntity())
    }
}
