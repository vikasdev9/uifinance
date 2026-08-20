package com.uifinance.project291.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.uifinance.project291.design_system.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MineScreen(
    onNavigateToCategories: () -> Unit,
    onNavigateToPaymentMethods: () -> Unit,
    onNavigateToAccounts: () -> Unit,
    onNavigateToProfile: () -> Unit,
    viewModel: MineViewModel = hiltViewModel()
) {
    val theme by viewModel.theme.collectAsState()
    val accentColor by viewModel.accentColor.collectAsState()
    val remindersEnabled by viewModel.remindersEnabled.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Mine", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = DeepObsidian,
                    titleContentColor = HighEmphasisText
                )
            )
        },
        containerColor = DeepObsidian
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            item {
                ProfileHeader(onNavigateToProfile)
            }

            item {
                MineSection(title = "PREFERENCES") {
                    MineItem(
                        icon = Icons.Rounded.Palette,
                        title = "Theme",
                        subtitle = theme,
                        onClick = { /* TODO */ }
                    )
                    MineItem(
                        icon = Icons.Rounded.Brush,
                        title = "Accent Color",
                        subtitle = accentColor,
                        onClick = { /* TODO */ }
                    )
                    MineItem(
                        icon = Icons.Rounded.Widgets,
                        title = "Widgets",
                        subtitle = accentColor,
                        onClick = { /* TODO */ }
                    )
                }
            }

            item {
                MineSection(title = "FINANCE") {
                    MineItem(
                        icon = Icons.Rounded.Category,
                        title = "Categories",
                        onClick = onNavigateToCategories
                    )
                    MineItem(
                        icon = Icons.Rounded.Payments,
                        title = "Payment Methods",
                        onClick = onNavigateToPaymentMethods
                    )
                    MineItem(
                        icon = Icons.Rounded.AccountBalanceWallet,
                        title = "Accounts / Wallets",
                        onClick = onNavigateToAccounts
                    )
                }
            }

            item {
                MineSection(title = "GENERAL") {
                    MineItem(
                        icon = Icons.Rounded.NotificationsActive,
                        title = "Remainder Option",
                        subtitle = if (remindersEnabled) "Enabled" else "Disabled",
                        onClick = { viewModel.toggleReminders(!remindersEnabled) }
                    )
                    MineItem(
                        icon = Icons.Rounded.IosShare,
                        title = "Export",
                        onClick = { /* TODO */ }
                    )
                }
            }

            item {
                MineSection(title = "SUPPORT") {
                    MineItem(
                        icon = Icons.Rounded.PrivacyTip,
                        title = "Privacy Policy",
                        onClick = { /* TODO */ }
                    )
                    MineItem(
                        icon = Icons.Rounded.Star,
                        title = "Rate Us",
                        onClick = { /* TODO */ }
                    )
                    MineItem(
                        icon = Icons.Rounded.Share,
                        title = "Share App",
                        onClick = { /* TODO */ }
                    )
                }
            }
            
            item { Spacer(modifier = Modifier.height(100.dp)) }
        }
    }
}

@Composable
fun ProfileHeader(onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(CardSurface)
            .clickable(onClick = onClick)
            .padding(20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                .background(EmeraldGreen.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Rounded.Person,
                contentDescription = null,
                tint = EmeraldGreen,
                modifier = Modifier.size(32.dp)
            )
        }
        Spacer(modifier = Modifier.width(20.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "Premium User",
                style = MaterialTheme.typography.titleLarge,
                color = HighEmphasisText,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "premium@novavest.com",
                style = MaterialTheme.typography.bodyMedium,
                color = SecondaryText
            )
        }
        Icon(
            imageVector = Icons.Rounded.ChevronRight,
            contentDescription = null,
            tint = SecondaryText
        )
    }
}

@Composable
fun MineSection(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Column {
        Text(
            text = title,
            style = MaterialTheme.typography.labelMedium,
            color = MutedGold,
            letterSpacing = 1.sp,
            modifier = Modifier.padding(start = 4.dp, bottom = 12.dp)
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(CardSurface)
        ) {
            content()
        }
    }
}

@Composable
fun MineItem(
    icon: ImageVector,
    title: String,
    subtitle: String? = null,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(DeepObsidian),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = EmeraldGreen,
                modifier = Modifier.size(22.dp)
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                color = HighEmphasisText,
                fontWeight = FontWeight.Medium
            )
            if (subtitle != null) {
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = SecondaryText
                )
            }
        }
        Icon(
            imageVector = Icons.Rounded.ChevronRight,
            contentDescription = null,
            tint = SecondaryText,
            modifier = Modifier.size(20.dp)
        )
    }
}
