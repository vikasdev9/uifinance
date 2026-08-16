package com.uifinance.project291.ui.payment

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.uifinance.project291.data.local.entity.PaymentMethod
import com.uifinance.project291.design_system.*
import com.uifinance.project291.ui.components.PaymentMethodIcons
import com.uifinance.project291.ui.payment.components.AddEditPaymentMethodBottomSheet

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentMethodManagementScreen(
    viewModel: PaymentMethodManagementViewModel,
    onBackClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    var showAddSheet by remember { mutableStateOf(false) }
    var pmToEdit by remember { mutableStateOf<PaymentMethod?>(null) }
    var pmToDelete by remember { mutableStateOf<PaymentMethod?>(null) }

    if (pmToDelete != null) {
        AlertDialog(
            onDismissRequest = { pmToDelete = null },
            title = { Text("Delete Payment Method?") },
            text = { Text("Are you sure you want to delete '${pmToDelete?.name}'? Historical transactions will still keep this payment method but you won't be able to use it for new ones.") },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.deletePaymentMethod(pmToDelete!!)
                    pmToDelete = null
                }) {
                    Text("DELETE", color = NegativeRed)
                }
            },
            dismissButton = {
                TextButton(onClick = { pmToDelete = null }) {
                    Text("CANCEL", color = SecondaryText)
                }
            },
            containerColor = CardSurface,
            titleContentColor = HighEmphasisText,
            textContentColor = SecondaryText
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Manage Payment Methods", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Rounded.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = DeepObsidian,
                    titleContentColor = HighEmphasisText,
                    navigationIconContentColor = HighEmphasisText
                )
            )
        },
        containerColor = DeepObsidian,
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    pmToEdit = null
                    showAddSheet = true
                },
                containerColor = EmeraldGreen,
                contentColor = DeepObsidian,
                shape = CircleShape
            ) {
                Icon(Icons.Rounded.Add, contentDescription = "Add Payment Method")
            }
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            when (val state = uiState) {
                is PaymentMethodManagementUiState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = MutedGold)
                    }
                }
                is PaymentMethodManagementUiState.Success -> {
                    if (state.paymentMethods.isEmpty()) {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text("No payment methods yet", color = SecondaryText)
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(state.paymentMethods) { pm ->
                                PaymentMethodRow(
                                    pm = pm,
                                    onEdit = {
                                        pmToEdit = it
                                        showAddSheet = true
                                    },
                                    onDelete = { pmToDelete = it }
                                )
                            }
                        }
                    }
                }
                is PaymentMethodManagementUiState.Error -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(text = state.message, color = NegativeRed)
                    }
                }
            }
        }
    }

    if (showAddSheet) {
        ModalBottomSheet(
            onDismissRequest = { showAddSheet = false },
            containerColor = DeepObsidian,
            shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)
        ) {
            AddEditPaymentMethodBottomSheet(
                paymentMethod = pmToEdit,
                onDismiss = { showAddSheet = false },
                onConfirm = { name, icon, color, type ->
                    if (pmToEdit == null) {
                        viewModel.addPaymentMethod(name, icon, color, type)
                    } else {
                        viewModel.updatePaymentMethod(pmToEdit!!.copy(name = name, iconRes = icon, colorHex = color, type = type))
                    }
                    showAddSheet = false
                }
            )
        }
    }
}

@Composable
fun PaymentMethodRow(
    pm: PaymentMethod,
    onEdit: (PaymentMethod) -> Unit,
    onDelete: (PaymentMethod) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(CardSurface)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(Color(android.graphics.Color.parseColor(pm.colorHex)).copy(alpha = 0.2f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = PaymentMethodIcons.getIcon(pm.iconRes),
                contentDescription = null,
                tint = Color(android.graphics.Color.parseColor(pm.colorHex)),
                modifier = Modifier.size(24.dp)
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = pm.name,
            style = MaterialTheme.typography.titleMedium,
            color = HighEmphasisText,
            modifier = Modifier.weight(1f)
        )
        
        IconButton(onClick = { onEdit(pm) }) {
            Icon(Icons.Rounded.Edit, contentDescription = "Edit", tint = SecondaryText)
        }
        
        if (!pm.isDefault) {
            IconButton(onClick = { onDelete(pm) }) {
                Icon(Icons.Rounded.Delete, contentDescription = "Delete", tint = NegativeRed)
            }
        }
    }
}
