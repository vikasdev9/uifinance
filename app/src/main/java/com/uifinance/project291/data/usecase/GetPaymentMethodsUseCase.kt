package com.uifinance.project291.data.usecase

import com.uifinance.project291.data.model.domain.PaymentMethod
import com.uifinance.project291.data.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPaymentMethodsUseCase @Inject constructor(
    private val repository: TransactionRepository
) {
    operator fun invoke(): Flow<List<PaymentMethod>> = repository.getPaymentMethods()
}
