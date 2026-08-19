package com.uifinance.project291.ui.budget.category

sealed class CategorySheetState {
    object Picker : CategorySheetState()
    data class AddNew(val parentId: Long? = null) : CategorySheetState()
}
