package com.uifinance.project291.data.local.dao

import androidx.room.*
import com.uifinance.project291.data.local.entity.Budget
import kotlinx.coroutines.flow.Flow

@Dao
interface BudgetDao {
    @Query("SELECT * FROM budgets")
    fun getAllBudgets(): Flow<List<Budget>>

    @Query("SELECT * FROM budgets WHERE isActive = 1")
    fun getActiveBudgets(): Flow<List<Budget>>

    @Query("SELECT * FROM budgets WHERE id = :id")
    suspend fun getBudgetById(id: Long): Budget?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBudget(budget: Budget)

    @Update
    suspend fun updateBudget(budget: Budget)

    @Delete
    suspend fun deleteBudget(budget: Budget)

    @Query("""
        SELECT SUM(amount) FROM transactions 
        WHERE categoryId = :categoryId 
        AND date >= :startDate 
        AND (:endDate IS NULL OR date <= :endDate)
        AND type = 'EXPENSE'
    """)
    fun getSpentAmountForCategory(categoryId: String, startDate: Long, endDate: Long?): Flow<Double?>
}
