package com.uifinance.project291.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

enum class BudgetPeriod {
    WEEKLY, MONTHLY, YEARLY, CUSTOM
}

enum class RecurrenceType {
    NONE, DAILY, WEEKLY, WEEKEND, MONTHLY
}

@Entity(tableName = "budgets")
data class Budget(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val categoryId: String,
    val categoryIcon: String,
    val amountLimit: Double,
    val amountSpent: Double = 0.0,
    val period: BudgetPeriod,
    val startDate: Date,
    val endDate: Date?,
    val recurrence: RecurrenceType,
    val alertThresholdPercent: Int = 80,
    val color: Int,
    val isActive: Boolean = true,
    val note: String? = null,
    val paymentMethodId: Long = 0,
    val attachmentUri: String? = null
)
