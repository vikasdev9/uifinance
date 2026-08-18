package com.uifinance.project291.ui.budget.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uifinance.project291.design_system.*

@Composable
fun AmountInput(
    amount: String,
    onAmountClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onAmountClick),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "HOW MUCH?",
            style = MaterialTheme.typography.labelSmall,
            color = SecondaryText
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "₹",
                style = MaterialTheme.typography.displayLarge.copy(color = MutedGold),
                fontSize = 40.sp
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = amount.ifEmpty { "0" },
                style = MaterialTheme.typography.displayLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = HighEmphasisText
                )
            )
        }
    }
}
