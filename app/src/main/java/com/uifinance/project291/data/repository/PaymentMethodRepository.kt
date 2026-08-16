package com.uifinance.project291.data.repository

import com.uifinance.project291.data.local.dao.PaymentMethodDao
import com.uifinance.project291.data.local.entity.PaymentMethod
import com.uifinance.project291.data.local.entity.PaymentMethodType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PaymentMethodRepository @Inject constructor(
    private val paymentMethodDao: PaymentMethodDao
) {
    companion object {
        private val seedingMutex = Mutex()
    }

    fun getAllPaymentMethods(): Flow<List<PaymentMethod>> = paymentMethodDao.getAllPaymentMethods()

    suspend fun insertPaymentMethod(paymentMethod: PaymentMethod) = paymentMethodDao.insertPaymentMethod(paymentMethod)

    suspend fun deletePaymentMethod(paymentMethod: PaymentMethod) = paymentMethodDao.deletePaymentMethod(paymentMethod)

    suspend fun seedDefaultPaymentMethods() {
        seedingMutex.withLock {
            val existingMethods = getAllPaymentMethods().first()
            
            val defaults = listOf(
                PaymentMethod(name = "Cash", iconRes = "payments", colorHex = "#10B981", type = PaymentMethodType.ASSET, isDefault = true),
                PaymentMethod(name = "Debit Card", iconRes = "credit_card", colorHex = "#3B82F6", type = PaymentMethodType.ASSET),
                PaymentMethod(name = "Credit Card", iconRes = "credit_card", colorHex = "#F87171", type = PaymentMethodType.LIABILITY),
                PaymentMethod(name = "Online", iconRes = "language", colorHex = "#F7931A", type = PaymentMethodType.ASSET),
                PaymentMethod(name = "UPI", iconRes = "qr_code", colorHex = "#D4AF37", type = PaymentMethodType.ASSET),
                PaymentMethod(name = "PayPal", iconRes = "payments", colorHex = "#3B82F6", type = PaymentMethodType.ASSET)
            )

            // 1. Cleanup: Identify and remove duplicates from existing data
            val seen = mutableSetOf<Pair<String, PaymentMethodType>>()
            existingMethods.forEach { method ->
                val key = method.name to method.type
                if (seen.contains(key)) {
                    // This is a duplicate, delete it
                    deletePaymentMethod(method)
                } else {
                    seen.add(key)
                }
            }

            // 2. Seeding: Insert missing defaults
            defaults.forEach { default ->
                val alreadyExists = seen.any { it.first == default.name && it.second == default.type }
                if (!alreadyExists) {
                    insertPaymentMethod(default)
                }
            }
        }
    }
}
