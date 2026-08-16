package com.uifinance.project291.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

enum class TransactionType {
    EXPENSE, INCOME, TRANSFER
}

@Entity(tableName = "transactions")
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val subtitle: String,
    val amount: Double,
    val type: TransactionType,
    val categoryId: String,
    val date: Date = Date(),
    val note: String? = null
)
