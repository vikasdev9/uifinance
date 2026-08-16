package com.uifinance.project291.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class PaymentMethodType {
    ASSET, LIABILITY
}

@Entity(tableName = "payment_methods")
data class PaymentMethod(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val iconRes: String, // Icon name for mapping
    val colorHex: String, // Hex color code
    val type: PaymentMethodType = PaymentMethodType.ASSET,
    val isDefault: Boolean = false,
    val allowNegativeBalance: Boolean = false
)
