package com.uifinance.project291.ui.budget

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.uifinance.project291.data.local.entity.PaymentMethod
import com.uifinance.project291.design_system.*
import com.uifinance.project291.ui.components.PaymentMethodIcons
import com.uifinance.project291.ui.transaction.AddTransferViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun TransferTab(
    viewModel: AddTransferViewModel,
    onFromWalletClick: () -> Unit,
    onToWalletClick: () -> Unit,
    onDateClick: () -> Unit,
    onAmountClick: () -> Unit,
    onRecurrenceClick: () -> Unit,
    onAttachmentClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(horizontal = 20.dp)
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        // Reuse amount display logic
        Box(modifier = Modifier.clickable(onClick = onAmountClick)) {
            TransferAmountDisplay(amount = uiState.amount)
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Wallet Selectors with Swap Icon
        Box(modifier = Modifier.fillMaxWidth()) {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                WalletSelectorCard(
                    label = "From wallet",
                    wallet = uiState.fromWallet,
                    balance = uiState.fromBalance,
                    newBalance = uiState.newFromBalance,
                    amountEntered = uiState.amountValue,
                    onClick = onFromWalletClick
                )

                WalletSelectorCard(
                    label = "To wallet",
                    wallet = uiState.toWallet,
                    balance = uiState.toBalance,
                    newBalance = uiState.newToBalance,
                    amountEntered = uiState.amountValue,
                    isIncrease = true,
                    onClick = onToWalletClick
                )
            }

            // Swap Icon
            var rotationAngle by remember { mutableStateOf(0f) }
            val rotation by animateFloatAsState(
                targetValue = rotationAngle,
                animationSpec = spring(stiffness = Spring.StiffnessLow),
                label = "SwapRotation"
            )

            IconButton(
                onClick = { 
                    rotationAngle += 180f
                    viewModel.onSwapWallets() 
                },
                modifier = Modifier
                    .align(Alignment.Center)
                    .offset(x = 10.dp) // Adjust based on design
                    .size(40.dp)
                    .background(DeepObsidian, CircleShape)
                    .background(EmeraldGreen.copy(alpha = 0.1f), CircleShape)
                    .clip(CircleShape)
                    .rotate(rotation)
            ) {
                Icon(
                    imageVector = Icons.Rounded.SwapVert,
                    contentDescription = "Swap",
                    tint = EmeraldGreen,
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        // Insufficient Balance Warning
        if (uiState.isInsufficientBalance) {
            Text(
                text = "Insufficient balance in ${uiState.fromWallet?.name}",
                color = NegativeRed,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 8.dp, start = 16.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Date selector
        val dateStr = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(uiState.date)
        SelectorRow(
            label = "Date",
            value = dateStr,
            icon = Icons.Rounded.CalendarToday,
            onClick = onDateClick
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Recurrence selector
        SelectorRow(
            label = "Recurrence",
            value = uiState.recurrence.name.lowercase().replaceFirstChar { it.uppercase() },
            icon = Icons.Rounded.Update,
            onClick = onRecurrenceClick
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Note Section
        NoteSection(
            note = uiState.note,
            attachmentUri = uiState.attachmentUri?.let { android.net.Uri.parse(it) },
            onNoteChange = viewModel::onNoteChange,
            onAttachmentClick = onAttachmentClick,
            onRemoveAttachment = viewModel::onRemoveAttachment
        )

        Spacer(modifier = Modifier.height(40.dp))
    }
}

@Composable
private fun TransferAmountDisplay(amount: String) {
    Column(
        modifier = Modifier.fillMaxWidth(),
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

@Composable
private fun WalletSelectorCard(
    label: String,
    wallet: PaymentMethod?,
    balance: Double,
    newBalance: Double,
    amountEntered: Double,
    isIncrease: Boolean = false,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
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
                    tint = wallet?.let { Color(android.graphics.Color.parseColor(it.colorHex)) } ?: EmeraldGreen,
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
