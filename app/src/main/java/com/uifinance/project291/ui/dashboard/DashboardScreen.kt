package com.uifinance.project291.ui.dashboard

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material.icons.outlined.AttachMoney
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Payments
import androidx.compose.material.icons.outlined.TrendingUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.uifinance.project291.data.MockData
import com.uifinance.project291.data.model.AssetAllocation
import com.uifinance.project291.data.model.DashboardUiState
import com.uifinance.project291.data.model.Transaction
import com.uifinance.project291.data.model.TransactionType
import com.uifinance.project291.design_system.CardSurface
import com.uifinance.project291.design_system.DeepObsidian
import com.uifinance.project291.design_system.EmeraldGreen
import com.uifinance.project291.design_system.HighEmphasisText
import com.uifinance.project291.design_system.IconCircleBackground
import com.uifinance.project291.design_system.MutedGold
import com.uifinance.project291.design_system.NegativeRed
import com.uifinance.project291.design_system.NovaVestTheme
import com.uifinance.project291.design_system.SecondaryText
import com.uifinance.project291.design_system.TrendBadgeBackground
import com.uifinance.project291.ui.components.AllocationDonutChart
import java.text.NumberFormat
import java.util.Locale

@Composable
fun DashboardScreen(
    onViewAllActivity: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: DashboardViewModel = viewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    DashboardContent(
        uiState = uiState,
        onViewAllActivity = onViewAllActivity,
        modifier = modifier,
    )
}

@Composable
fun DashboardContent(
    uiState: DashboardUiState,
    onViewAllActivity: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DeepObsidian)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp),
    ) {
        Spacer(modifier = Modifier.height(8.dp))
        DashboardTopBar(profileImageUrl = uiState.profileImageUrl)
        Spacer(modifier = Modifier.height(24.dp))
        NetWorthCard(
            totalNetWorth = uiState.totalNetWorth,
            growthPercentage = uiState.growthPercentage,
        )
        Spacer(modifier = Modifier.height(16.dp))
        AllocationCard(
            allocations = uiState.allocations,
            assetCount = uiState.assetCount,
        )
        Spacer(modifier = Modifier.height(16.dp))
        RecentActivityCard(
            transactions = uiState.recentTransactions,
            onViewAllActivity = onViewAllActivity,
        )
        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun DashboardTopBar(profileImageUrl: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {

        Icon(
            imageVector = Icons.Default.AccountCircle,
            contentDescription = "Notifications",
            tint = EmeraldGreen,
            modifier = Modifier.size(24.dp),
        )
        Text(
            text = "NOVAVEST",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                letterSpacing = MaterialTheme.typography.labelSmall.letterSpacing,
            ),
            color = EmeraldGreen,
        )

        Icon(
            imageVector = Icons.Default.NotificationsNone,
            contentDescription = "Notifications",
            tint = EmeraldGreen,
            modifier = Modifier.size(24.dp),
        )
    }
}

@Composable
private fun NetWorthCard(
    totalNetWorth: Double,
    growthPercentage: Double,
) {
    val currencyFormatter = remember {
        NumberFormat.getCurrencyInstance(Locale.US).apply {
            minimumFractionDigits = 2
            maximumFractionDigits = 2
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(
                brush = Brush.radialGradient(
                    colors = listOf(
                        CardSurface.copy(alpha = 0.95f),
                        DeepObsidian.copy(alpha = 0.6f),
                    ),
                    radius = 600f,
                ),
            )
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        EmeraldGreen.copy(alpha = 0.08f),
                        MutedGold.copy(alpha = 0.05f),
                    ),
                ),
            )
            .padding(horizontal = 24.dp, vertical = 28.dp),
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "TOTAL NET WORTH",
                style = MaterialTheme.typography.labelSmall,
                color = SecondaryText,
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = currencyFormatter.format(totalNetWorth),
                style = MaterialTheme.typography.displayLarge.copy(
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                ),
                color = MutedGold,
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .background(TrendBadgeBackground)
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Icon(
                    imageVector = Icons.Outlined.TrendingUp,
                    contentDescription = null,
                    tint = EmeraldGreen,
                    modifier = Modifier.size(14.dp),
                )
                Text(
                    text = "+${String.format(Locale.US, "%.1f", growthPercentage)}%",
                    style = MaterialTheme.typography.labelMedium,
                    color = EmeraldGreen,
                )
            }
        }
    }
}

@Composable
private fun AllocationCard(
    allocations: List<AssetAllocation>,
    assetCount: Int,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(CardSurface)
            .padding(20.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "Allocation",
                style = MaterialTheme.typography.headlineMedium,
                color = HighEmphasisText,
            )
            Icon(
                imageVector = Icons.Default.MoreHoriz,
                contentDescription = "More options",
                tint = SecondaryText,
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center,
        ) {
            AllocationDonutChart(
                allocations = allocations,
                assetCount = assetCount,
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        allocations.forEach { allocation ->
            AllocationLegendRow(allocation = allocation)
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}

@Composable
private fun AllocationLegendRow(allocation: AssetAllocation) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(10.dp)
                .clip(CircleShape)
                .background(allocation.color),
        )
        Text(
            text = allocation.name,
            style = MaterialTheme.typography.bodyMedium,
            color = HighEmphasisText,
            modifier = Modifier.padding(start = 10.dp),
        )
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = "${allocation.percentage.toInt()}%",
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
            color = MutedGold,
        )
    }
}

@Composable
private fun RecentActivityCard(
    transactions: List<Transaction>,
    onViewAllActivity: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(CardSurface)
            .padding(20.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "Recent Activity",
                style = MaterialTheme.typography.headlineMedium,
                color = HighEmphasisText,
            )
            Text(
                text = "VIEW ALL",
                style = MaterialTheme.typography.labelSmall,
                color = EmeraldGreen,
                modifier = Modifier
                    .clickable(onClick = onViewAllActivity)
                    .padding(4.dp),
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        transactions.forEachIndexed { index, transaction ->
            AnimatedTransactionItem(
                transaction = transaction,
                index = index,
            )
            if (index < transactions.lastIndex) {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
private fun AnimatedTransactionItem(
    transaction: Transaction,
    index: Int,
) {
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(transaction.id) {
        visible = true
    }

    AnimatedVisibility(
        visible = visible,
        enter = slideInHorizontally(
            animationSpec = tween(durationMillis = 500, delayMillis = index * 100),
            initialOffsetX = { it / 2 },
        ) + fadeIn(
            animationSpec = tween(durationMillis = 500, delayMillis = index * 100),
        ),
    ) {
        TransactionRow(transaction = transaction)
    }
}

@Composable
private fun TransactionRow(transaction: Transaction) {
    val currencyFormatter = remember {
        NumberFormat.getCurrencyInstance(Locale.US).apply {
            minimumFractionDigits = 2
            maximumFractionDigits = 2
        }
    }
    val isPositive = transaction.amount >= 0
    val formattedAmount = buildString {
        append(if (isPositive) "+" else "")
        append(currencyFormatter.format(transaction.amount))
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(IconCircleBackground),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = transaction.type.toIcon(),
                contentDescription = transaction.title,
                tint = transaction.iconTint,
                modifier = Modifier.size(22.dp),
            )
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 14.dp),
        ) {
            Text(
                text = transaction.title,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                color = HighEmphasisText,
            )
            Text(
                text = transaction.subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = SecondaryText,
            )
        }

        Text(
            text = formattedAmount,
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
            color = if (isPositive) EmeraldGreen else NegativeRed,
        )
    }
}

private fun TransactionType.toIcon(): ImageVector = when (this) {
    TransactionType.PROPERTY_YIELD -> Icons.Outlined.Home
    TransactionType.GOLD_PURCHASE -> Icons.Outlined.AttachMoney
    TransactionType.DIVIDEND -> Icons.Outlined.Payments
}

@Preview(showBackground = true, backgroundColor = 0xFF0B0E11)
@Composable
private fun DashboardContentPreview() {
    NovaVestTheme {
        DashboardContent(
            uiState = MockData.dashboardUiState,
            onViewAllActivity = {},
        )
    }
}
