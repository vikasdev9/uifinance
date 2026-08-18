package com.uifinance.project291.ui.budget.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
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

sealed class PaymentMethodSheetState {
    object Picker : PaymentMethodSheetState()
    object AddNew : PaymentMethodSheetState()
}

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
    val sections = listOf("Asset", "Liability")
    var selectedSection by remember { mutableStateOf("Asset") }

    when (currentState) {
        is PaymentMethodSheetState.Picker -> {
            PaymentMethodPickerList(
                paymentMethods = paymentMethods,
                selectedMethod = selectedMethod,
                selectedSection = selectedSection,
                sections = sections,
                onSectionSelected = { selectedSection = it },
                onMethodSelected = onMethodSelected,
                onAddNewClick = { currentState = PaymentMethodSheetState.AddNew },
                onNavigateToManagement = onNavigateToManagement,
                onDismiss = onDismiss
            )
        }
        is PaymentMethodSheetState.AddNew -> {
            val type = if (selectedSection == "Liability") PaymentMethodType.LIABILITY else PaymentMethodType.ASSET
            AddEditPaymentMethodContent(
                type = type,
                onSave = { name, icon, color, selectedType ->
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
    selectedSection: String,
    sections: List<String>,
    onSectionSelected: (String) -> Unit,
    onMethodSelected: (PaymentMethod) -> Unit,
    onAddNewClick: () -> Unit,
    onNavigateToManagement: () -> Unit,
    onDismiss: () -> Unit
) {
    val filteredMethods = paymentMethods.filter { method ->
        val targetType = if (selectedSection == "Liability") PaymentMethodType.LIABILITY else PaymentMethodType.ASSET
        method.type == targetType
    }.distinctBy { it.name to it.type }

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
                sections.forEach { section ->
                    val isSelected = selectedSection == section
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(if (isSelected) AnalyticsCardSurface else Color.Transparent)
                            .clickable { onSectionSelected(section) }
                            .padding(16.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = when(section) {
                                    "Asset" -> Icons.Rounded.AccountBalanceWallet
                                    "Liability" -> Icons.Rounded.Payments
                                    else -> Icons.Rounded.AccountBalanceWallet
                                },
                                contentDescription = null,
                                tint = if (isSelected) EmeraldGreen else SecondaryText,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = section,
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

                // Add New Button
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

@Composable
fun AddEditPaymentMethodContent(
    paymentMethod: PaymentMethod? = null,
    type: PaymentMethodType = PaymentMethodType.ASSET,
    onDismiss: () -> Unit,
    onSave: (name: String, icon: String, color: String, type: PaymentMethodType) -> Unit
) {
    var name by remember { mutableStateOf(paymentMethod?.name ?: "") }
    var selectedIcon by remember { mutableStateOf(paymentMethod?.iconRes ?: "account_balance_wallet") }
    var selectedColor by remember { mutableStateOf(paymentMethod?.colorHex ?: "#10B981") }
    var selectedType by remember { mutableStateOf(paymentMethod?.type ?: type) }

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
                            .background(try { Color(android.graphics.Color.parseColor(selectedColor)) } catch(e: Exception) { EmeraldGreen }),
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
                                    .background(try { Color(android.graphics.Color.parseColor(colorStr)) } catch(e: Exception) { EmeraldGreen })
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
                                    if (selectedIcon == iconName) (try { Color(android.graphics.Color.parseColor(selectedColor)) } catch(e: Exception) { EmeraldGreen }).copy(alpha = 0.2f)
                                    else Color.Transparent
                                )
                                .clickable { selectedIcon = iconName },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = iconVector,
                                contentDescription = null,
                                tint = if (selectedIcon == iconName) (try { Color(android.graphics.Color.parseColor(selectedColor)) } catch(e: Exception) { EmeraldGreen }) else SecondaryText,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                }
            }

            Button(
                onClick = { onSave(name, selectedIcon, selectedColor, selectedType) },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = EmeraldGreen),
                shape = RoundedCornerShape(16.dp),
                enabled = name.isNotBlank()
            ) {
                Text("SAVE ACCOUNT", color = DeepObsidian, fontWeight = FontWeight.Bold)
            }
            
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}
