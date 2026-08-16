package com.uifinance.project291.ui.payment

sealed class PaymentMethodSheetState {
    object Picker : PaymentMethodSheetState()
    object AddNew : PaymentMethodSheetState()
}
