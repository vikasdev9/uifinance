package com.uifinance.project291.data.local.dao

import androidx.room.*
import com.uifinance.project291.data.local.entity.PaymentMethod
import kotlinx.coroutines.flow.Flow

@Dao
interface PaymentMethodDao {
    @Query("SELECT * FROM payment_methods")
    fun getAllPaymentMethods(): Flow<List<PaymentMethod>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPaymentMethod(paymentMethod: PaymentMethod): Long

    @Delete
    suspend fun deletePaymentMethod(paymentMethod: PaymentMethod)

    @Query("SELECT COUNT(*) FROM payment_methods")
    suspend fun getCount(): Int
}
