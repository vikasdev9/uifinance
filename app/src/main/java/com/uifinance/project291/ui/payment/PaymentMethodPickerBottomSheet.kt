package com.uifinance.project291.ui.payment

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Payments
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.rounded.AccountBalanceWallet
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
fun PaymentMethodPickerContent(
    paymentMethods: List<PaymentMethod>,
    selectedMethod: PaymentMethod?,
    onMethodSelected: (PaymentMethod) -> Unit,
    onAddCustom: (name: String, icon: String, color: String, type: PaymentMethodType) -> Unit,
    onNavigateToManagement: () -> Unit,
    onDismiss: () -> Unit
) {
    var currentState by remember { mutableStateOf<PaymentMethodSheetState>(PaymentMethodSheetState.Picker) }
    var selectedType by remember { mutableStateOf(PaymentMethodType.ASSET) }

    when (currentState) {
        is PaymentMethodSheetState.Picker -> {
            PaymentMethodPickerList(
                paymentMethods = paymentMethods,
                selectedMethod = selectedMethod,
                selectedType = selectedType,
                onTypeSelected = { selectedType = it },
                onMethodSelected = onMethodSelected,
                onAddNewClick = { currentState = PaymentMethodSheetState.AddNew },
                onNavigateToManagement = onNavigateToManagement,
                onDismiss = onDismiss
            )
        }
        is PaymentMethodSheetState.AddNew -> {
            AddPaymentMethodContent(
                type = selectedType,
                onSave = { name, icon, color ->
                    onAddCustom(name, icon, color, selectedType)
                    currentState = PaymentMethodSheetState.Picker
                },
                onDismiss = { currentState = PaymentMethodSheetState.Picker }
            )
        }
    }
}

@Composable
private fun PaymentMethodPickerList(
    paymentMethods: List<PaymentMethod>,
    selectedMethod: PaymentMethod?,
    selectedType: PaymentMethodType,
    onTypeSelected: (PaymentMethodType) -> Unit,
    onMethodSelected: (PaymentMethod) -> Unit,
    onAddNewClick: () -> Unit,
    onNavigateToManagement: () -> Unit,
    onDismiss: () -> Unit
) {
    val filteredMethods = paymentMethods.filter { it.type == selectedType }
        .distinctBy { it.name to it.type }

    GenericBottomSheetContent(
        title = "Account",
        onDismiss = onDismiss,
        headerAction = {
            IconButton(onClick = onNavigateToManagement) {
                Icon(Icons.Rounded.Settings, contentDescription = "Settings", tint = HighEmphasisText)
            }
        }
    ) {
        Row(modifier = Modifier.fillMaxWidth().height(400.dp)) {
            // Left Sidebar (Tabs)
            Column(
                modifier = Modifier
                    .weight(0.35f)
                    .fillMaxHeight()
                    .background(AnalyticsBackground) 
            ) {
                PaymentMethodType.values().forEach { type ->
                    val isSelected = selectedType == type
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(if (isSelected) AnalyticsCardSurface else Color.Transparent)
                            .clickable { onTypeSelected(type) }
                            .padding(16.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = if (type == PaymentMethodType.ASSET) Icons.Rounded.AccountBalanceWallet else Icons.Rounded.Payments,
                                contentDescription = null,
                                tint = if (isSelected) EmeraldGreen else SecondaryText,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = type.name.lowercase().replaceFirstChar { it.uppercase() },
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) HighEmphasisText else SecondaryText
                            )
                        }
                    }
                }
            }

            // Right Content (Methods)
            Column(
                modifier = Modifier
                    .weight(0.65f)
                    .fillMaxHeight()
                    .background(DeepObsidian)
                    .padding(16.dp)
            ) {
                Column(modifier = Modifier.weight(1f).verticalScroll(rememberScrollState())) {
                    filteredMethods.forEach { method ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onMethodSelected(method) }
                                .padding(vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = PaymentMethodIcons.getIcon(method.iconRes),
                                contentDescription = null,
                                tint = EmeraldGreen,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            Text(
                                text = method.name,
                                style = MaterialTheme.typography.bodyLarge,
                                color = HighEmphasisText,
                                modifier = Modifier.weight(1f)
                            )
                            if (selectedMethod?.id == method.id) {
                                Icon(Icons.Rounded.Check, contentDescription = null, tint = MutedGold) 
                            }
                        }
                    }
                }

                // Add New Button (Styled as in user request images)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    Surface(
                        onClick = onAddNewClick,
                        shape = RoundedCornerShape(8.dp),
                        color = Color.Transparent
                    ) {
                        Row(
                            modifier = Modifier.padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .background(MutedGold, RoundedCornerShape(4.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Rounded.Add, contentDescription = null, tint = DeepObsidian, modifier = Modifier.size(16.dp))
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "ADD NEW", 
                                color = MutedGold, 
                                fontWeight = FontWeight.Bold, 
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }
        }
    }
}
