package com.uifinance.project291.ui.budget.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.uifinance.project291.design_system.*
import com.uifinance.project291.data.model.domain.PaymentMethod
import com.uifinance.project291.ui.components.PaymentMethodIcons
import java.util.*

@Composable
fun WalletSelectorCard(
    label: String,
    wallet: PaymentMethod?,
    balance: Double,
    newBalance: Double,
    amountEntered: Double,
    isIncrease: Boolean = false,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onClick),
        color = CardSurface
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Wallet Icon
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(IconCircleBackground, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = PaymentMethodIcons.getIcon(wallet?.iconRes ?: "wallet"),
                    contentDescription = null,
                    tint = wallet?.let { try { Color(android.graphics.Color.parseColor(it.colorHex)) } catch (e: Exception) { EmeraldGreen } } ?: EmeraldGreen,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(text = label, style = MaterialTheme.typography.labelSmall, color = SecondaryText)
                Text(
                    text = wallet?.name ?: "Select wallet",
                    style = MaterialTheme.typography.bodyLarge,
                    color = HighEmphasisText,
                    fontWeight = FontWeight.Bold
                )
            }

            if (wallet != null) {
                Column(horizontalAlignment = Alignment.End) {
                    Text(text = "Balance", style = MaterialTheme.typography.labelSmall, color = SecondaryText)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "₹${String.format(Locale.getDefault(), "%,.0f", balance)}",
                            style = MaterialTheme.typography.bodySmall,
                            color = SecondaryText
                        )
                        if (amountEntered > 0) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Rounded.ArrowForward,
                                contentDescription = null,
                                tint = SecondaryText,
                                modifier = Modifier.size(12.dp).padding(horizontal = 4.dp)
                            )
                            Text(
                                text = "₹${String.format(Locale.getDefault(), "%,.0f", newBalance)}",
                                style = MaterialTheme.typography.bodySmall,
                                color = if (isIncrease) EmeraldGreen else NegativeRed,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}
