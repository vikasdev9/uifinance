package com.uifinance.project291.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.uifinance.project291.data.model.AssetAllocation
import com.uifinance.project291.design_system.HighEmphasisText
import com.uifinance.project291.design_system.SecondaryText

@Composable
fun AllocationDonutChart(
    allocations: List<AssetAllocation>,
    assetCount: Int,
    modifier: Modifier = Modifier,
) {
    val strokeWidth = 30.dp
    val gapAngle = 4f
    val density = LocalDensity.current
    val strokeWidthPx = with(density) { strokeWidth.toPx() }
    val strokeStyle = remember(strokeWidthPx) {
        Stroke(width = strokeWidthPx, cap = StrokeCap.Square)
    }

    Box(
        modifier = modifier.size(220.dp),
        contentAlignment = Alignment.Center,
    ) {
        Canvas(modifier = Modifier.size(220.dp)) {
            val diameter = size.minDimension - strokeWidthPx
            val topLeft = Offset(
                x = (size.width - diameter) / 2f,
                y = (size.height - diameter) / 2f,
            )
            val arcSize = Size(diameter, diameter)
            var startAngle = -90f

            allocations.forEach { allocation ->
                val sweepAngle = (allocation.percentage / 100f) * 360f - gapAngle
                drawArc(
                    color = allocation.color,
                    startAngle = startAngle,
                    sweepAngle = sweepAngle,
                    useCenter = false,
                    topLeft = topLeft,
                    size = arcSize,
                    style = strokeStyle,
                )
                startAngle += sweepAngle + gapAngle
            }
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "ASSETS",
                style = MaterialTheme.typography.labelSmall,
                color = SecondaryText,
            )
            Text(
                text = assetCount.toString(),
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = MaterialTheme.typography.displayLarge.fontSize * 0.7f,
                ),
                color = HighEmphasisText,
            )
        }
    }
}
