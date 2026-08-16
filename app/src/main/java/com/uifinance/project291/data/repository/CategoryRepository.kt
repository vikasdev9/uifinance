package com.uifinance.project291.data.repository

import com.uifinance.project291.data.local.dao.CategoryDao
import com.uifinance.project291.data.local.dao.TransactionDao
import com.uifinance.project291.data.local.entity.Category
import com.uifinance.project291.data.local.entity.CategoryType
import com.uifinance.project291.data.local.entity.CategoryWithChildren
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

interface CategoryRepository {
    fun getCategoriesByType(type: CategoryType): Flow<List<Category>>
    fun getCategoriesWithChildren(type: CategoryType): Flow<List<CategoryWithChildren>>
    suspend fun insert(category: Category): Long
    suspend fun update(category: Category)
    suspend fun delete(category: Category)
    suspend fun updateSortOrders(categories: List<Category>)
    suspend fun seedDefaultsIfNeeded()
}

@Singleton
class CategoryRepositoryImpl @Inject constructor(
    private val categoryDao: CategoryDao,
    private val transactionDao: TransactionDao
) : CategoryRepository {
    override fun getCategoriesByType(type: CategoryType): Flow<List<Category>> =
        categoryDao.getCategoriesByType(type)

    override fun getCategoriesWithChildren(type: CategoryType): Flow<List<CategoryWithChildren>> =
        categoryDao.getCategoriesWithChildren(type)

    override suspend fun insert(category: Category): Long = categoryDao.insert(category)

    override suspend fun update(category: Category) {
        categoryDao.update(category)
        transactionDao.updateTransactionTitlesByCategory(category.id.toString(), category.name)
    }

    override suspend fun delete(category: Category) = categoryDao.delete(category)

    override suspend fun updateSortOrders(categories: List<Category>) =
        categoryDao.updateSortOrders(categories)

    override suspend fun seedDefaultsIfNeeded() {
        val count = categoryDao.getCategoryCount()
        if (count == 0) {
            val defaults = listOf(
                // Expense Parents
                Category(id = 1, name = "Food & Drink", iconRes = "fastfood", colorHex = "#FF9800", type = CategoryType.EXPENSE, parentId = null, sortOrder = 0, isDefault = true),
                Category(id = 7, name = "Shopping", iconRes = "shopping_cart", colorHex = "#E91E63", type = CategoryType.EXPENSE, parentId = null, sortOrder = 1, isDefault = true),
                Category(id = 11, name = "Transport", iconRes = "directions_car", colorHex = "#2196F3", type = CategoryType.EXPENSE, parentId = null, sortOrder = 2, isDefault = true),
                Category(id = 12, name = "Housing", iconRes = "home", colorHex = "#4CAF50", type = CategoryType.EXPENSE, parentId = null, sortOrder = 3, isDefault = true),
                Category(id = 13, name = "Entertainment", iconRes = "movie", colorHex = "#9C27B0", type = CategoryType.EXPENSE, parentId = null, sortOrder = 4, isDefault = true),
                Category(id = 14, name = "Education", iconRes = "school", colorHex = "#795548", type = CategoryType.EXPENSE, parentId = null, sortOrder = 5, isDefault = true),

                // Food & Drink Children
                Category(id = 2, name = "Food & Drink", iconRes = "fastfood", colorHex = "#FF9800", type = CategoryType.EXPENSE, parentId = 1, sortOrder = 0, isDefault = true),
                Category(id = 3, name = "Breakfast", iconRes = "free_breakfast", colorHex = "#FF9800", type = CategoryType.EXPENSE, parentId = 1, sortOrder = 1, isDefault = true),
                Category(id = 4, name = "Lunch", iconRes = "lunch_dining", colorHex = "#FF9800", type = CategoryType.EXPENSE, parentId = 1, sortOrder = 2, isDefault = true),
                Category(id = 5, name = "Dinner", iconRes = "dinner_dining", colorHex = "#FF9800", type = CategoryType.EXPENSE, parentId = 1, sortOrder = 3, isDefault = true),
                Category(id = 6, name = "Coffee", iconRes = "coffee", colorHex = "#FF9800", type = CategoryType.EXPENSE, parentId = 1, sortOrder = 4, isDefault = true),

                // Income Parents
                Category(id = 15, name = "Salary", iconRes = "payments", colorHex = "#4CAF50", type = CategoryType.INCOME, parentId = null, sortOrder = 0, isDefault = true),
                Category(id = 16, name = "Investments", iconRes = "trending_up", colorHex = "#2196F3", type = CategoryType.INCOME, parentId = null, sortOrder = 1, isDefault = true),
                Category(id = 17, name = "Allowance", iconRes = "account_balance", colorHex = "#FFC107", type = CategoryType.INCOME, parentId = null, sortOrder = 2, isDefault = true),
                Category(id = 18, name = "Bonus", iconRes = "card_giftcard", colorHex = "#FF5722", type = CategoryType.INCOME, parentId = null, sortOrder = 3, isDefault = true),
                Category(id = 19, name = "Other", iconRes = "more_horiz", colorHex = "#607D8B", type = CategoryType.INCOME, parentId = null, sortOrder = 4, isDefault = true)
            )
            defaults.forEach { categoryDao.insert(it) }
        }
    }
}
