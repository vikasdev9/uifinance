package com.uifinance.project291.ui.analytics

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material.icons.outlined.TrendingUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.uifinance.project291.data.MockData
import com.uifinance.project291.data.model.AnalyticsUiState
import com.uifinance.project291.data.model.TimeRange
import com.uifinance.project291.design_system.AnalyticsBackground
import com.uifinance.project291.design_system.AnalyticsCardSurface
import com.uifinance.project291.design_system.DeepObsidian
import com.uifinance.project291.design_system.DividerColor
import com.uifinance.project291.design_system.EmeraldGreen
import com.uifinance.project291.design_system.HighEmphasisText
import com.uifinance.project291.design_system.NovaVestTheme
import com.uifinance.project291.design_system.SecondaryText
import com.uifinance.project291.design_system.TrendBadgeBackground
import com.uifinance.project291.ui.components.AssetPerformanceCard
import com.uifinance.project291.ui.components.TimeRangeSelector
import com.uifinance.project291.ui.components.WealthLineChart
import java.text.NumberFormat
import java.util.Locale

@Composable
fun AnalyticsScreen(
    modifier: Modifier = Modifier,
    viewModel: AnalyticsViewModel = viewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    AnalyticsContent(
        uiState = uiState,
        onTimeRangeSelected = viewModel::onTimeRangeSelected,
        modifier = modifier,
    )
}

@Composable
fun AnalyticsContent(
    uiState: AnalyticsUiState,
    onTimeRangeSelected: (TimeRange) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.radialGradient(
                    colors = listOf(
                        EmeraldGreen.copy(alpha = 0.08f),
                        AnalyticsBackground,
                        DeepObsidian,
                    ),
                    radius = 900f,
                ),
            )
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp),
    ) {
        Spacer(modifier = Modifier.height(8.dp))
        AnalyticsTopBar()
        Spacer(modifier = Modifier.height(24.dp))
        WealthGrowthHeader(
            totalPortfolioValue = uiState.totalPortfolioValue,
            growthPercentage = uiState.growthPercentage,
        )
        Spacer(modifier = Modifier.height(20.dp))
        ChartCard(
            uiState = uiState,
            onTimeRangeSelected = onTimeRangeSelected,
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "Top Performing Assets",
            style = MaterialTheme.typography.headlineMedium,
            color = HighEmphasisText,
        )
        Spacer(modifier = Modifier.height(16.dp))
        uiState.topAssets.forEachIndexed { index, asset ->
            AssetPerformanceCard(asset = asset)
            if (index < uiState.topAssets.lastIndex) {
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun AnalyticsTopBar() {
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
private fun WealthGrowthHeader(
    totalPortfolioValue: Double,
    growthPercentage: Double,
) {
    val currencyFormatter = androidx.compose.runtime.remember {
        NumberFormat.getCurrencyInstance(Locale.US).apply {
            minimumFractionDigits = 2
            maximumFractionDigits = 2
        }
    }

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Wealth Growth",
            style = MaterialTheme.typography.headlineMedium,
            color = HighEmphasisText,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(
                text = currencyFormatter.format(totalPortfolioValue),
                style = MaterialTheme.typography.displayLarge.copy(
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    fontSize = 34.sp,
                    lineHeight = 40.sp,
                ),
                color = HighEmphasisText,
                modifier = Modifier.weight(1f, fill = false),
            )
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .background(TrendBadgeBackground)
                    .padding(horizontal = 10.dp, vertical = 6.dp),
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
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = "Total Portfolio Value",
            style = MaterialTheme.typography.bodySmall,
            color = SecondaryText,
        )
    }
}

@Composable
private fun ChartCard(
    uiState: AnalyticsUiState,
    onTimeRangeSelected: (TimeRange) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(AnalyticsCardSurface)
            .border(1.dp, DividerColor, RoundedCornerShape(24.dp))
            .padding(16.dp),
    ) {
        TimeRangeSelector(
            selectedRange = uiState.selectedTimeRange,
            onRangeSelected = onTimeRangeSelected,
        )
        Spacer(modifier = Modifier.height(16.dp))
        WealthLineChart(
            chartPoints = uiState.chartPoints,
            monthLabels = uiState.monthLabels,
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF08110D)
@Composable
private fun AnalyticsContentPreview() {
    NovaVestTheme {
        AnalyticsContent(
            uiState = MockData.analyticsUiState(),
            onTimeRangeSelected = {},
        )
    }
}
