package com.uifinance.project291.navigation

import kotlinx.serialization.Serializable

@Serializable
object VaultDestination

@Serializable
object AnalyticsDestination

@Serializable
object AssetsDestination

@Serializable
object SettingsDestination

@Serializable
object BudgetsDestination

@Serializable
object AddEntryDestination

@Serializable
data class EditBudgetDestination(val id: Long)
