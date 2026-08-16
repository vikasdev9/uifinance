package com.uifinance.project291.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.uifinance.project291.data.model.ChartDataPoint
import com.uifinance.project291.design_system.ChartGridColor
import com.uifinance.project291.design_system.EmeraldGreen
import com.uifinance.project291.design_system.MutedGold
import com.uifinance.project291.design_system.SecondaryText

@Composable
fun WealthLineChart(
    chartPoints: List<ChartDataPoint>,
    monthLabels: List<String>,
    modifier: Modifier = Modifier,
) {
    val linePath = remember { Path() }
    val fillPath = remember { Path() }
    val lineColor = MutedGold
    val fillBrush = remember {
        Brush.verticalGradient(
            colors = listOf(
                EmeraldGreen.copy(alpha = 0.25f),
                EmeraldGreen.copy(alpha = 0.02f),
            ),
        )
    }

    Column(modifier = modifier) {
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .padding(horizontal = 4.dp),
        ) {
            if (chartPoints.isEmpty()) return@Canvas

            val chartHeight = size.height
            val chartWidth = size.width
            val topPadding = 16f
            val bottomPadding = 8f
            val drawableHeight = chartHeight - topPadding - bottomPadding

            val xStep = if (chartPoints.size <= 1) {
                0f
            } else {
                chartWidth / (chartPoints.size - 1).toFloat()
            }

            val coordinates = chartPoints.mapIndexed { index, point ->
                Offset(
                    x = index * xStep,
                    y = topPadding + drawableHeight * (1f - point.normalizedValue),
                )
            }

            monthLabels.forEachIndexed { index, _ ->
                val gridX = if (monthLabels.size <= 1) {
                    chartWidth
                } else {
                    chartWidth * index / (monthLabels.size - 1).toFloat()
                }
                drawLine(
                    color = ChartGridColor,
                    start = Offset(gridX, topPadding),
                    end = Offset(gridX, chartHeight - bottomPadding),
                    strokeWidth = 1f,
                )
            }

            linePath.reset()
            fillPath.reset()

            linePath.moveTo(coordinates.first().x, coordinates.first().y)
            fillPath.moveTo(coordinates.first().x, chartHeight)

            for (index in 1 until coordinates.size) {
                val previous = coordinates[index - 1]
                val current = coordinates[index]
                val controlX = (previous.x + current.x) / 2f
                linePath.cubicTo(
                    controlX,
                    previous.y,
                    controlX,
                    current.y,
                    current.x,
                    current.y,
                )
                fillPath.cubicTo(
                    controlX,
                    previous.y,
                    controlX,
                    current.y,
                    current.x,
                    current.y,
                )
            }

            fillPath.lineTo(coordinates.last().x, chartHeight)
            fillPath.lineTo(coordinates.first().x, chartHeight)
            fillPath.close()

            drawPath(path = fillPath, brush = fillBrush)
            drawPath(
                path = linePath,
                color = lineColor,
                style = Stroke(width = 3f, cap = StrokeCap.Round),
            )

            coordinates.forEach { point ->
                drawCircle(
                    color = lineColor,
                    radius = 4f,
                    center = point,
                )
            }

            val lastPoint = coordinates.last()
            drawCircle(
                color = lineColor.copy(alpha = 0.35f),
                radius = 10f,
                center = lastPoint,
            )
            drawCircle(
                color = lineColor,
                radius = 6f,
                center = lastPoint,
            )

            drawLine(
                color = SecondaryText.copy(alpha = 0.35f),
                start = Offset(lastPoint.x, topPadding),
                end = Offset(lastPoint.x, chartHeight - bottomPadding),
                strokeWidth = 1f,
                pathEffect = androidx.compose.ui.graphics.PathEffect.dashPathEffect(
                    floatArrayOf(8f, 8f),
                ),
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            monthLabels.forEach { label ->
                Text(
                    text = label,
                    style = MaterialTheme.typography.bodySmall,
                    color = SecondaryText,
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}
