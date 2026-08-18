package com.uifinance.project291.data.usecase

import com.uifinance.project291.data.repository.PaymentMethodRepository
import javax.inject.Inject

class SeedPaymentMethodsUseCase @Inject constructor(
    private val repository: PaymentMethodRepository
) {
    suspend operator fun invoke() {
        repository.seedDefaultPaymentMethods()
    }
}
