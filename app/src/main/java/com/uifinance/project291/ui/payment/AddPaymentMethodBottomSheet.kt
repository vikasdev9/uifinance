package com.uifinance.project291.ui.payment

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Description
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.uifinance.project291.data.local.entity.PaymentMethodType
import com.uifinance.project291.design_system.*
import com.uifinance.project291.ui.components.GenericBottomSheetContent
import com.uifinance.project291.ui.components.PaymentMethodIcons

@Composable
fun AddPaymentMethodContent(
    type: PaymentMethodType,
    onSave: (name: String, icon: String, color: String) -> Unit,
    onDismiss: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var selectedIcon by remember { mutableStateOf("wallet") }
    var selectedColor by remember { mutableStateOf("#3B82F6") }

    val icons = listOf("wallet", "credit_card", "account_balance", "account_balance_wallet")
    val colors = listOf("#10B981", "#D4AF37", "#3B82F6", "#F87171", "#F7931A", "#9C27B0", "#E91E63", "#00BCD4")

    GenericBottomSheetContent(
        title = "New ${type.name.lowercase().replaceFirstChar { it.uppercase() }}",
        onDismiss = onDismiss
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            // Name Section
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Rounded.Description, contentDescription = null, tint = SecondaryText, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Account Name *",
                    style = MaterialTheme.typography.bodyMedium,
                    color = SecondaryText
                )
            }
            
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                placeholder = { Text("e.g. Savings Account", color = SecondaryText.copy(alpha = 0.5f)) },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = HighEmphasisText,
                    unfocusedTextColor = HighEmphasisText,
                    focusedBorderColor = MutedGold,
                    unfocusedBorderColor = DividerColor,
                    cursorColor = MutedGold
                ),
                shape = RoundedCornerShape(12.dp),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Icon Section
            Text(
                text = "Select Icon",
                style = MaterialTheme.typography.bodyMedium,
                color = SecondaryText,
                modifier = Modifier.padding(bottom = 12.dp)
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                icons.forEach { icon ->
                    val isSelected = selectedIcon == icon
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (isSelected) MutedGold else CardSurface)
                            .clickable { selectedIcon = icon },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = PaymentMethodIcons.getIcon(icon),
                            contentDescription = null,
                            tint = if (isSelected) DeepObsidian else HighEmphasisText,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Color Section
            Text(
                text = "Select Color",
                style = MaterialTheme.typography.bodyMedium,
                color = SecondaryText,
                modifier = Modifier.padding(bottom = 12.dp)
            )
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                colors.forEach { color ->
                    val isSelected = selectedColor == color
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(Color(android.graphics.Color.parseColor(color)))
                            .clickable { selectedColor = color },
                        contentAlignment = Alignment.Center
                    ) {
                        if (isSelected) {
                            Icon(Icons.Rounded.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(48.dp))

            // Save Button
            Button(
                onClick = { if (name.isNotBlank()) onSave(name, selectedIcon, selectedColor) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MutedGold,
                    disabledContainerColor = MutedGold.copy(alpha = 0.5f)
                ),
                shape = RoundedCornerShape(16.dp),
                enabled = name.isNotBlank()
            ) {
                Text(
                    text = "SAVE ACCOUNT",
                    color = DeepObsidian,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
            
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}
