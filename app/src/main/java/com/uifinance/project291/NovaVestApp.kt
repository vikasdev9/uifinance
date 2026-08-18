package com.uifinance.project291

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountBalanceWallet
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material.icons.outlined.PieChart
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.uifinance.project291.design_system.BottomNavBackground
import com.uifinance.project291.design_system.DeepObsidian
import com.uifinance.project291.design_system.EmeraldGreen
import com.uifinance.project291.design_system.HighEmphasisText
import com.uifinance.project291.design_system.SecondaryText
import com.uifinance.project291.navigation.*
import com.uifinance.project291.ui.budget.AddTransactionScreen
import com.uifinance.project291.ui.analytics.AnalyticsScreen
import com.uifinance.project291.ui.category.CategoryManagementScreen
import com.uifinance.project291.ui.dashboard.DashboardScreen
import com.uifinance.project291.ui.onboarding.OnboardingScreen
import com.uifinance.project291.ui.payment.PaymentMethodManagementScreen
import com.uifinance.project291.ui.payment.PaymentMethodManagementViewModel
import com.uifinance.project291.ui.settings.SettingsScreen
import androidx.hilt.navigation.compose.hiltViewModel

private data class BottomNavItem(
    val destination: Any,
    val label: String,
    val icon: ImageVector,
)

private val bottomNavItems = listOf(
    BottomNavItem(VaultDestination, "Vault", Icons.Outlined.AccountBalanceWallet),
    BottomNavItem(AnalyticsDestination, "Analytics", Icons.Outlined.BarChart),
    BottomNavItem(BudgetsDestination, "Budgets", Icons.Outlined.PieChart),
    BottomNavItem(SettingsDestination, "Settings", Icons.Outlined.Settings),
)

@Composable
fun NovaVestApp() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val showBottomBar = currentDestination?.hasRoute(OnboardingDestination::class) == false &&
            currentDestination.hasRoute(AddEntryDestination::class) == false &&
            currentDestination.hasRoute(ManageCategoriesDestination::class) == false &&
            currentDestination.hasRoute(ManagePaymentMethodsDestination::class) == false

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = DeepObsidian,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        bottomBar = {
            if (showBottomBar) {
                NovaVestBottomBar(
                    items = bottomNavItems,
                    currentDestination = currentDestination,
                    onItemSelected = { item ->
                        navController.navigate(item.destination) {
                            popUpTo(VaultDestination) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },
                )
            }
        },
        floatingActionButton = {
            if (showBottomBar) {
                FloatingActionButton(
                    onClick = { navController.navigate(AddEntryDestination) },
                    containerColor = EmeraldGreen,
                    contentColor = DeepObsidian,
                    shape = CircleShape,
                    modifier = Modifier
                        .size(56.dp)
                        .offset(y = 40.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add", modifier = Modifier.size(28.dp))
                }
            }
        },
        floatingActionButtonPosition = FabPosition.Center,
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = OnboardingDestination,
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(if (showBottomBar) innerPadding else PaddingValues(0.dp)),
        ) {
            composable<OnboardingDestination> {
                OnboardingScreen(
                    onFinish = {
                        navController.navigate(VaultDestination) {
                            popUpTo(OnboardingDestination) { inclusive = true }
                        }
                    }
                )
            }
            composable<VaultDestination> {
                DashboardScreen(onViewAllActivity = {})
            }
            composable<AnalyticsDestination> {
                AnalyticsScreen()
            }
            composable<SettingsDestination> {
                SettingsScreen(
                    onNavigateToCategories = { navController.navigate(ManageCategoriesDestination) },
                    onNavigateToPaymentMethods = { navController.navigate(ManagePaymentMethodsDestination) },
                    onNavigateToAccounts = {},
                    onNavigateToProfile = {}
                )
            }
            composable<ManageCategoriesDestination> {
                CategoryManagementScreen(
                    viewModel = hiltViewModel(),
                    onBackClick = { navController.popBackStack() }
                )
            }
            composable<ManagePaymentMethodsDestination> {
                PaymentMethodManagementScreen(
                    viewModel = hiltViewModel(),
                    onBackClick = { navController.popBackStack() }
                )
            }
            composable<AddEntryDestination> {
                AddTransactionScreen(
                    onBack = { navController.popBackStack() },
                    onSuccess = { navController.popBackStack() },
                    onNavigateToCategoryManagement = { navController.navigate(ManageCategoriesDestination) },
                    onNavigateToPaymentMethodManagement = { navController.navigate(ManagePaymentMethodsDestination) }
                )
            }
        }
    }
}

@Composable
private fun NovaVestBottomBar(
    items: List<BottomNavItem>,
    currentDestination: androidx.navigation.NavDestination?,
    onItemSelected: (BottomNavItem) -> Unit,
) {
    NavigationBar(
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .background(BottomNavBackground),
        containerColor = BottomNavBackground,
        tonalElevation = 0.dp,
    ) {
        items.forEach { item ->
            val selected = currentDestination?.hasRoute(item.destination::class) == true
            NavigationBarItem(
                selected = selected,
                onClick = { onItemSelected(item) },
                icon = {
                    if (selected) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(EmeraldGreen.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center,
                        ) {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = item.label,
                                tint = EmeraldGreen,
                                modifier = Modifier.size(22.dp),
                            )
                        }
                    } else {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.label,
                            tint = SecondaryText,
                            modifier = Modifier.size(22.dp),
                        )
                    }
                },
                label = {
                    Text(
                        text = item.label,
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal,
                        ),
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = EmeraldGreen,
                    selectedTextColor = EmeraldGreen,
                    unselectedIconColor = SecondaryText,
                    unselectedTextColor = SecondaryText,
                    indicatorColor = BottomNavBackground,
                ),
            )
        }
    }
}

@Composable
private fun PlaceholderScreen(title: String) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DeepObsidian),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineMedium,
                color = HighEmphasisText,
            )
        }
    }
}
