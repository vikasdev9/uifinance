package com.uifinance.project291.data.usecase

import com.uifinance.project291.data.model.domain.Transaction
import com.uifinance.project291.data.repository.TransactionRepository
import javax.inject.Inject

class AddTransactionUseCase @Inject constructor(
    private val repository: TransactionRepository
) {
    suspend operator fun invoke(transaction: Transaction) {
        repository.insertTransaction(transaction)
    }
}
