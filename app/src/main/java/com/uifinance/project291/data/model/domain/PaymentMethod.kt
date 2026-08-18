package com.uifinance.project291.data.model.domain

enum class PaymentMethodType {
    ASSET, LIABILITY
}

data class PaymentMethod(
    val id: Long = 0,
    val name: String,
    val iconRes: String,
    val colorHex: String,
    val type: PaymentMethodType = PaymentMethodType.ASSET,
    val allowNegativeBalance: Boolean = false
)
