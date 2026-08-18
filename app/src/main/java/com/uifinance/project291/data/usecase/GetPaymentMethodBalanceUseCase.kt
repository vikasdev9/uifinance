package com.uifinance.project291.data.usecase

import com.uifinance.project291.data.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPaymentMethodBalanceUseCase @Inject constructor(
    private val repository: TransactionRepository
) {
    operator fun invoke(id: Long): Flow<Double> = repository.getPaymentMethodBalance(id)
}
