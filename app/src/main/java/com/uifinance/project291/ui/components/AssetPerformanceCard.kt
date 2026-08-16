package com.uifinance.project291.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountBalance
import androidx.compose.material.icons.outlined.CurrencyBitcoin
import androidx.compose.material.icons.outlined.HomeWork
import androidx.compose.material.icons.outlined.TrendingUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.uifinance.project291.data.model.AssetCategory
import com.uifinance.project291.data.model.AssetPerformance
import com.uifinance.project291.design_system.AnalyticsCardSurface
import com.uifinance.project291.design_system.EmeraldGreen
import com.uifinance.project291.design_system.HighEmphasisText
import com.uifinance.project291.design_system.SecondaryText
import java.text.NumberFormat
import java.util.Locale

@Composable
fun AssetPerformanceCard(
    asset: AssetPerformance,
    modifier: Modifier = Modifier,
) {
    val currencyFormatter = remember {
        NumberFormat.getCurrencyInstance(Locale.US).apply {
            minimumFractionDigits = 0
            maximumFractionDigits = 0
        }
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(AnalyticsCardSurface)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(asset.accentColor.copy(alpha = 0.18f))
                .border(1.dp, asset.accentColor.copy(alpha = 0.35f), CircleShape),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = asset.category.toIcon(),
                contentDescription = asset.name,
                tint = asset.accentColor,
                modifier = Modifier.size(24.dp),
            )
        }

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 14.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Text(
                text = asset.name,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                color = HighEmphasisText,
            )
            Text(
                text = asset.category.label,
                style = MaterialTheme.typography.labelSmall,
                color = asset.accentColor,
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .border(1.dp, asset.accentColor.copy(alpha = 0.45f), RoundedCornerShape(6.dp))
                    .padding(horizontal = 8.dp, vertical = 2.dp),
            )
        }

        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = currencyFormatter.format(asset.currentValue),
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                color = HighEmphasisText,
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(2.dp),
            ) {
                Icon(
                    imageVector = Icons.Outlined.TrendingUp,
                    contentDescription = null,
                    tint = EmeraldGreen,
                    modifier = Modifier.size(14.dp),
                )
                Text(
                    text = "+${String.format(Locale.US, "%.1f", asset.changePercentage)}%",
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                    color = EmeraldGreen,
                )
            }
        }
    }
}

private fun AssetCategory.toIcon(): ImageVector = when (this) {
    AssetCategory.CRYPTO -> Icons.Outlined.CurrencyBitcoin
    AssetCategory.EQUITY -> Icons.Outlined.AccountBalance
    AssetCategory.REAL_ASSET -> Icons.Outlined.HomeWork
}
