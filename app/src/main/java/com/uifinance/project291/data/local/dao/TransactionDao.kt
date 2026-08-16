package com.uifinance.project291.data.local.dao

import androidx.room.*
import com.uifinance.project291.data.local.entity.TransactionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {
    @Query("SELECT * FROM transactions ORDER BY date DESC")
    fun getAllTransactions(): Flow<List<TransactionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: TransactionEntity)

    @Query("""
        SELECT 
            (SELECT TOTAL(amount) FROM transactions WHERE paymentMethodId = :paymentMethodId AND type != 'TRANSFER') +
            (SELECT TOTAL(amount) FROM transactions WHERE toPaymentMethodId = :paymentMethodId AND type = 'TRANSFER') -
            (SELECT TOTAL(amount) FROM transactions WHERE paymentMethodId = :paymentMethodId AND type = 'TRANSFER')
    """)
    fun getPaymentMethodBalance(paymentMethodId: Long): Flow<Double>
}
