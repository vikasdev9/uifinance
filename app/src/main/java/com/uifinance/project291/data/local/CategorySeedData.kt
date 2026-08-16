package com.uifinance.project291.data.local

import android.content.ContentValues
import androidx.sqlite.db.SupportSQLiteDatabase
import com.uifinance.project291.data.local.entity.CategoryType

object CategorySeedData {
    fun seed(db: SupportSQLiteDatabase) {
        val categories = listOf(
            // Expense Parents
            seedCategory(1, "Food & Drink", "restaurant", "#FF9800", CategoryType.EXPENSE, null, 0),
            seedCategory(7, "Shopping", "shopping_bag", "#E91E63", CategoryType.EXPENSE, null, 1),
            seedCategory(11, "Transport", "directions_car", "#2196F3", CategoryType.EXPENSE, null, 2),
            seedCategory(12, "Housing", "home", "#4CAF50", CategoryType.EXPENSE, null, 3),
            seedCategory(13, "Entertainment", "movie", "#9C27B0", CategoryType.EXPENSE, null, 4),
            seedCategory(14, "Education", "school", "#795548", CategoryType.EXPENSE, null, 5),

            // Food & Drink Children
            seedCategory(2, "Food & Drink", "restaurant", "#FF9800", CategoryType.EXPENSE, 1, 0),
            seedCategory(3, "Breakfast", "free_breakfast", "#FF9800", CategoryType.EXPENSE, 1, 1),
            seedCategory(4, "Lunch", "lunch_dining", "#FF9800", CategoryType.EXPENSE, 1, 2),
            seedCategory(5, "Dinner", "dinner_dining", "#FF9800", CategoryType.EXPENSE, 1, 3),
            seedCategory(6, "Coffee", "coffee", "#FF9800", CategoryType.EXPENSE, 1, 4),

            // Shopping Children
            seedCategory(8, "Shopping", "shopping_bag", "#E91E63", CategoryType.EXPENSE, 7, 0),
            seedCategory(9, "Makeup", "brush", "#E91E63", CategoryType.EXPENSE, 7, 1),
            seedCategory(10, "Clothing", "checkroom", "#E91E63", CategoryType.EXPENSE, 7, 2),

            // Income Parents
            seedCategory(15, "Salary", "payments", "#4CAF50", CategoryType.INCOME, null, 0),
            seedCategory(16, "Investments", "trending_up", "#2196F3", CategoryType.INCOME, null, 1),
            seedCategory(17, "Allowance", "account_balance", "#FFC107", CategoryType.INCOME, null, 2),
            seedCategory(18, "Bonus", "card_giftcard", "#FF5722", CategoryType.INCOME, null, 3),
            seedCategory(19, "Other", "more_horiz", "#607D8B", CategoryType.INCOME, null, 4)
        )

        categories.forEach {
            db.insert("categories", android.database.sqlite.SQLiteDatabase.CONFLICT_REPLACE, it)
        }
    }

    private fun seedCategory(
        id: Long,
        name: String,
        iconRes: String,
        colorHex: String,
        type: CategoryType,
        parentId: Long?,
        sortOrder: Int
    ): ContentValues {
        return ContentValues().apply {
            put("id", id)
            put("name", name)
            put("iconRes", iconRes)
            put("colorHex", colorHex)
            put("type", type.name)
            put("parentId", parentId)
            put("sortOrder", sortOrder)
            put("isDefault", 1)
        }
    }
}
