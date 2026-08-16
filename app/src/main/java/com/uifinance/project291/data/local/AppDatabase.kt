package com.uifinance.project291.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.uifinance.project291.data.local.dao.BudgetDao
import com.uifinance.project291.data.local.dao.TransactionDao
import com.uifinance.project291.data.local.entity.Budget
import com.uifinance.project291.data.local.entity.TransactionEntity

@Database(entities = [Budget::class, TransactionEntity::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun budgetDao(): BudgetDao
    abstract fun transactionDao(): TransactionDao
}
