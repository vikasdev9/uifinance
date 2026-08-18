package com.uifinance.project291.data.model.domain

enum class CategoryType {
    EXPENSE, INCOME
}

data class Category(
    val id: Long = 0,
    val name: String,
    val iconRes: String,
    val colorHex: String,
    val type: CategoryType,
    val parentId: Long? = null
)
