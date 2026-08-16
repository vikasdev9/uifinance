package com.uifinance.project291.data.repository

import com.uifinance.project291.data.local.dao.BudgetDao
import com.uifinance.project291.data.local.dao.TransactionDao
import com.uifinance.project291.data.local.entity.Budget
import com.uifinance.project291.data.local.entity.TransactionEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BudgetRepository @Inject constructor(
    private val budgetDao: BudgetDao,
    private val transactionDao: TransactionDao
) {
    fun getAllBudgets(): Flow<List<Budget>> = budgetDao.getAllBudgets()
    fun getActiveBudgets(): Flow<List<Budget>> = budgetDao.getActiveBudgets()
    suspend fun getBudgetById(id: Long): Budget? = budgetDao.getBudgetById(id)
    suspend fun insertBudget(budget: Budget) = budgetDao.insertBudget(budget)
    suspend fun updateBudget(budget: Budget) = budgetDao.updateBudget(budget)
    suspend fun deleteBudget(budget: Budget) = budgetDao.deleteBudget(budget)

    fun getSpentAmount(categoryId: String, startDate: Long, endDate: Long?): Flow<Double?> {
        return budgetDao.getSpentAmountForCategory(categoryId, startDate, endDate)
    }

    suspend fun insertTransaction(transaction: TransactionEntity) {
        transactionDao.insertTransaction(transaction)
        // logic to check budget threshold and trigger notification would go here
    }

    fun getAllTransactions(): Flow<List<TransactionEntity>> = transactionDao.getAllTransactions()
}
