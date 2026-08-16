package com.uifinance.project291.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.uifinance.project291.data.local.AppDatabase
import com.uifinance.project291.data.local.CategorySeedData
import com.uifinance.project291.data.local.dao.BudgetDao
import com.uifinance.project291.data.local.dao.CategoryDao
import com.uifinance.project291.data.local.dao.PaymentMethodDao
import com.uifinance.project291.data.local.dao.TransactionDao
import com.uifinance.project291.data.repository.CategoryRepository
import com.uifinance.project291.data.repository.CategoryRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "novavest_db"
        ).addCallback(object : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                CategorySeedData.seed(db)
            }
        }).fallbackToDestructiveMigration().build()
    }

    @Provides
    fun provideBudgetDao(database: AppDatabase): BudgetDao {
        return database.budgetDao()
    }

    @Provides
    fun provideTransactionDao(database: AppDatabase): TransactionDao {
        return database.transactionDao()
    }

    @Provides
    fun provideCategoryDao(database: AppDatabase): CategoryDao {
        return database.categoryDao()
    }

    @Provides
    fun providePaymentMethodDao(database: AppDatabase): PaymentMethodDao {
        return database.paymentMethodDao()
    }

    @Provides
    @Singleton
    fun provideCategoryRepository(categoryDao: CategoryDao, transactionDao: TransactionDao): CategoryRepository {
        return CategoryRepositoryImpl(categoryDao, transactionDao)
    }
}
