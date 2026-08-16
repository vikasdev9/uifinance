package com.uifinance.project291.ui.payment.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uifinance.project291.data.local.entity.PaymentMethod
import com.uifinance.project291.data.local.entity.PaymentMethodType
import com.uifinance.project291.design_system.*
import com.uifinance.project291.ui.components.GenericBottomSheetContent
import com.uifinance.project291.ui.components.PaymentMethodIcons

@Composable
fun AddEditPaymentMethodBottomSheet(
    paymentMethod: PaymentMethod? = null,
    onDismiss: () -> Unit,
    onConfirm: (name: String, icon: String, color: String, type: PaymentMethodType) -> Unit
) {
    var name by remember { mutableStateOf(paymentMethod?.name ?: "") }
    var selectedIcon by remember { mutableStateOf(paymentMethod?.iconRes ?: "account_balance_wallet") }
    var selectedColor by remember { mutableStateOf(paymentMethod?.colorHex ?: "#10B981") }
    var selectedType by remember { mutableStateOf(paymentMethod?.type ?: PaymentMethodType.ASSET) }

    val colors = listOf("#10B981", "#D4AF37", "#3B82F6", "#F87171", "#F7931A", "#9C27B0", "#E91E63", "#00BCD4")

    GenericBottomSheetContent(
        title = if (paymentMethod == null) "Add Payment Method" else "Edit Payment Method",
        onDismiss = onDismiss
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Name") },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MutedGold,
                    unfocusedBorderColor = DividerColor,
                    focusedLabelColor = MutedGold,
                    unfocusedLabelColor = SecondaryText,
                    focusedTextColor = HighEmphasisText,
                    unfocusedTextColor = HighEmphasisText
                ),
                singleLine = true
            )

            // Icon & Color
            Column {
                Text("Icon & Color", style = MaterialTheme.typography.labelMedium, color = SecondaryText)
                Row(
                    modifier = Modifier.padding(top = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color(android.graphics.Color.parseColor(selectedColor))),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = PaymentMethodIcons.getIcon(selectedIcon),
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                    
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(4),
                        modifier = Modifier.weight(1f).height(64.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(colors) { colorStr ->
                            Box(
                                modifier = Modifier
                                    .size(28.dp)
                                    .clip(CircleShape)
                                    .background(Color(android.graphics.Color.parseColor(colorStr)))
                                    .clickable { selectedColor = colorStr }
                                    .border(
                                        width = if (selectedColor == colorStr) 2.dp else 0.dp,
                                        color = HighEmphasisText,
                                        shape = CircleShape
                                    )
                            )
                        }
                    }
                }
            }

            // Icon Grid
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp),
                color = CardSurface,
                shape = RoundedCornerShape(16.dp)
            ) {
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(48.dp),
                    contentPadding = PaddingValues(12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(PaymentMethodIcons.icons) { (iconName, iconVector) ->
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(
                                    if (selectedIcon == iconName) Color(android.graphics.Color.parseColor(selectedColor)).copy(alpha = 0.2f)
                                    else Color.Transparent
                                )
                                .clickable { selectedIcon = iconName },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = iconVector,
                                contentDescription = null,
                                tint = if (selectedIcon == iconName) Color(android.graphics.Color.parseColor(selectedColor)) else SecondaryText,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                }
            }

            Button(
                onClick = { onConfirm(name, selectedIcon, selectedColor, selectedType) },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = EmeraldGreen),
                shape = RoundedCornerShape(16.dp),
                enabled = name.isNotBlank()
            ) {
                Text("SAVE PAYMENT METHOD", color = DeepObsidian, fontWeight = FontWeight.Bold)
            }
            
            Spacer(modifier = Modifier.height(WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()))
        }
    }
}
