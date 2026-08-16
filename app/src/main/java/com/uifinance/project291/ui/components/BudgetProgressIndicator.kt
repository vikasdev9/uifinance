package com.uifinance.project291.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uifinance.project291.design_system.EmeraldGreen
import com.uifinance.project291.design_system.MutedGold
import com.uifinance.project291.design_system.NegativeRed
import com.uifinance.project291.design_system.SecondaryText

@Composable
fun BudgetLinearProgress(
    spent: Double,
    limit: Double,
    modifier: Modifier = Modifier
) {
    val progress = (spent / limit).coerceIn(0.0, 1.0).toFloat()
    val progressColor = when {
        progress < 0.7f -> EmeraldGreen
        progress < 1.0f -> MutedGold
        else -> NegativeRed
    }

    Column(modifier = modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(SecondaryText.copy(alpha = 0.2f))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(progress)
                    .fillMaxHeight()
                    .clip(RoundedCornerShape(4.dp))
                    .background(progressColor)
            )
        }
    }
}
