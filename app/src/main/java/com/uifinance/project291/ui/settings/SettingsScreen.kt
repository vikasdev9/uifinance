package com.uifinance.project291.ui.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uifinance.project291.design_system.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    onNavigateToCategories: () -> Unit,
    onNavigateToPaymentMethods: () -> Unit,
    onNavigateToAccounts: () -> Unit,
    onNavigateToProfile: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings", fontWeight = FontWeight.Bold) },
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
            item { Spacer(modifier = Modifier.height(8.dp)) }

            item {
                SettingsSection(title = "ACCOUNT") {
                    SettingsItem(
                        icon = Icons.Rounded.Person,
                        title = "Profile",
                        onClick = onNavigateToProfile
                    )
                    SettingsItem(
                        icon = Icons.Rounded.AccountBalanceWallet,
                        title = "Accounts / Wallets",
                        onClick = onNavigateToAccounts
                    )
                }
            }

            item {
                SettingsSection(title = "TRANSACTION") {
                    SettingsItem(
                        icon = Icons.Rounded.Category,
                        title = "Categories",
                        onClick = onNavigateToCategories
                    )
                    SettingsItem(
                        icon = Icons.Rounded.Payments,
                        title = "Payment Methods",
                        onClick = onNavigateToPaymentMethods
                    )
                }
            }
            
            item { Spacer(modifier = Modifier.height(32.dp)) }
        }
    }
}

@Composable
fun SettingsSection(
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
fun SettingsItem(
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
