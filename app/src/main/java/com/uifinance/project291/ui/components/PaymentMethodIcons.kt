package com.uifinance.project291.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.ui.graphics.vector.ImageVector

object PaymentMethodIcons {
    val icons = listOf(
        "payments" to Icons.Rounded.Payments,
        "credit_card" to Icons.Rounded.CreditCard,
        "account_balance" to Icons.Rounded.AccountBalance,
        "language" to Icons.Rounded.Language,
        "account_balance_wallet" to Icons.Rounded.AccountBalanceWallet,
        "wallet" to Icons.Rounded.Wallet,
        "qr_code" to Icons.Rounded.QrCode,
        "paypal" to Icons.Rounded.Payments // Fallback for PayPal
    )

    fun getIcon(name: String): ImageVector {
        return icons.find { it.first == name }?.second ?: Icons.Rounded.Payments
    }
}
