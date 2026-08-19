package com.uifinance.project291.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.uifinance.project291.data.local.dao.CategoryDao
import com.uifinance.project291.data.local.dao.TransactionDao
import com.uifinance.project291.data.local.dao.PaymentMethodDao
import com.uifinance.project291.data.local.entity.Category
import com.uifinance.project291.data.local.entity.PaymentMethod
import com.uifinance.project291.data.local.entity.TransactionEntity

@Database(entities = [TransactionEntity::class, Category::class, PaymentMethod::class], version = 5, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun transactionDao(): TransactionDao
    abstract fun categoryDao(): CategoryDao
    abstract fun paymentMethodDao(): PaymentMethodDao
}
