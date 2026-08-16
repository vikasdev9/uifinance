package com.uifinance.project291.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class CategoryType {
    EXPENSE, INCOME
}

@Entity(tableName = "categories")
data class Category(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val iconRes: String,
    val colorHex: String,
    val type: CategoryType,
    val parentId: Long? = null,
    val sortOrder: Int = 0,
    val isDefault: Boolean = false
)
