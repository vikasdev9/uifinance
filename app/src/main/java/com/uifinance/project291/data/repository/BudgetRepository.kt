package com.uifinance.project291.data.repository

import com.uifinance.project291.data.local.dao.BudgetDao
import com.uifinance.project291.data.local.entity.Budget
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BudgetRepository @Inject constructor(
    private val budgetDao: BudgetDao
) {
    fun getAllBudgets(): Flow<List<Budget>> = budgetDao.getAllBudgets()
    fun getActiveBudgets(): Flow<List<Budget>> = budgetDao.getActiveBudgets()
    suspend fun insertBudget(budget: Budget) = budgetDao.insertBudget(budget)
    suspend fun deleteBudget(budget: Budget) = budgetDao.deleteBudget(budget)

    fun getSpentAmount(categoryId: String, startDate: Long, endDate: Long?): Flow<Double?> {
        return budgetDao.getSpentAmountForCategory(categoryId, startDate, endDate)
    }
}
