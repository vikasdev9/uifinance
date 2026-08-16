package com.uifinance.project291.ui.budget

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Notes
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.uifinance.project291.data.local.entity.CategoryType
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import com.uifinance.project291.data.local.entity.PaymentMethod
import com.uifinance.project291.data.local.entity.PaymentMethodType
import com.uifinance.project291.data.local.entity.RecurrenceType
import com.uifinance.project291.design_system.*
import com.uifinance.project291.ui.category.AddCategoryContent
import com.uifinance.project291.ui.category.CategoryPickerContent
import com.uifinance.project291.ui.category.CategoryPickerViewModel
import com.uifinance.project291.ui.category.CategorySheetState
import com.uifinance.project291.ui.category.components.CategoryIcons
import com.uifinance.project291.ui.components.*
import com.uifinance.project291.ui.payment.PaymentMethodPickerContent
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddBudgetScreen(
    onNavigateBack: () -> Unit,
    viewModel: AddBudgetViewModel = hiltViewModel(),
    categoryViewModel: CategoryPickerViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val paymentMethods by viewModel.paymentMethods.collectAsStateWithLifecycle()
    
    var showPaymentMethodPicker by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }
    var showRecurrencePicker by remember { mutableStateOf(false) }
    var showAttachmentOptions by remember { mutableStateOf(false) }
    var showCamera by remember { mutableStateOf(false) }
    
    // Consolidated Category Sheet State
    var activeCategorySheet by remember { mutableStateOf<CategorySheetState?>(null) }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri -> uri?.let { viewModel.onAttachmentAdded(it) } }
    )

    if (uiState.saveSuccess) {
        LaunchedEffect(Unit) { onNavigateBack() }
    }

    if (showCamera) {
        CameraScreen(
            onImageCaptured = { uri ->
                viewModel.onAttachmentAdded(uri)
                showCamera = false
            },
            onClose = { showCamera = false }
        )
        return
    }

    Scaffold(
        topBar = {
            Column(modifier = Modifier.background(AnalyticsBackground)) {
                val entryTypes = BudgetEntryType.values()
                val selectedIndex = uiState.entryType.ordinal
                
                TopAppBar(
                    title = { 
                        Text(
                            text = "Add ${if (uiState.entryType == BudgetEntryType.BUDGET) "Transfer" else uiState.entryType.name.lowercase().replaceFirstChar { it.uppercase() }}", 
                            color = HighEmphasisText, 
                            fontWeight = FontWeight.Bold
                        ) 
                    },
                    navigationIcon = {
                        IconButton(onClick = onNavigateBack) {
                            Icon(Icons.Rounded.ArrowBack, contentDescription = "Back", tint = HighEmphasisText)
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = AnalyticsBackground)
                )
                
                TabRow(
                    selectedTabIndex = selectedIndex,
                    containerColor = AnalyticsBackground,
                    contentColor = HighEmphasisText,
                    indicator = { tabPositions ->
                        TabRowDefaults.SecondaryIndicator(
                            Modifier.tabIndicatorOffset(tabPositions[selectedIndex]),
                            color = EmeraldGreen
                        )
                    },
                    divider = {}
                ) {
                    entryTypes.forEach { type ->
                        val isSelected = uiState.entryType == type
                        Tab(
                            selected = isSelected,
                            onClick = { viewModel.onEntryTypeChange(type) },
                            text = { 
                                Text(
                                    text = if (type == BudgetEntryType.BUDGET) "Transfer" else type.name.lowercase().replaceFirstChar { it.uppercase() },
                                    color = if (isSelected) EmeraldGreen else SecondaryText
                                ) 
                            }
                        )
                    }
                }
            }
        },
        containerColor = AnalyticsBackground,
        bottomBar = {
            BottomActionBar(
                onContinue = { /* Logic for continue if needed */ },
                onSave = viewModel::saveBudget,
                isSaveEnabled = uiState.amount.isNotEmpty() && uiState.selectedCategory != null
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {
            // Amount Section
            AmountSection(
                amount = uiState.amount,
                isKeypadVisible = uiState.isKeypadVisible,
                onToggleKeypad = viewModel::toggleKeypad,
                onKeypadOpen = { if (!uiState.isKeypadVisible) viewModel.toggleKeypad() }
            )

            // Form Content
            Column(modifier = Modifier.padding(20.dp)) {
                // Budget Name (Only for Budget)
                if (uiState.entryType == BudgetEntryType.BUDGET) {
                    OutlinedTextField(
                        value = uiState.name,
                        onValueChange = viewModel::onNameChange,
                        placeholder = { Text("Transfer Name", color = SecondaryText) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = AnalyticsCardSurface,
                            unfocusedContainerColor = AnalyticsCardSurface,
                            focusedBorderColor = MutedGold,
                            unfocusedBorderColor = DividerColor,
                            focusedTextColor = HighEmphasisText,
                            unfocusedTextColor = HighEmphasisText
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }

                // Category
                SelectionRow(
                    label = "Category",
                    value = uiState.selectedCategory?.name ?: "Select Category",
                    icon = if (uiState.selectedCategory != null) CategoryIcons.getIcon(uiState.selectedCategory!!.iconRes) else Icons.Rounded.Category,
                    iconColor = uiState.selectedCategory?.let { Color(android.graphics.Color.parseColor(it.colorHex)) } ?: MutedGold,
                    onClick = { 
                        categoryViewModel.setCategoryType(
                            when(uiState.entryType) {
                                BudgetEntryType.BUDGET, BudgetEntryType.EXPENSE -> CategoryType.EXPENSE
                                BudgetEntryType.INCOME -> CategoryType.INCOME
                            }
                        )
                        activeCategorySheet = CategorySheetState.Picker
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Recurrence (Period)
                SelectionRow(
                    label = "Period",
                    value = when(uiState.recurrence) {
                        RecurrenceType.NONE -> "NONE"
                        RecurrenceType.DAILY -> "EVERYDAY"
                        RecurrenceType.WEEKEND -> "WEEKENDS"
                        RecurrenceType.MONTHLY -> "MONTHLY"
                        else -> uiState.recurrence.name
                    },
                    icon = Icons.Rounded.Update,
                    onClick = { showRecurrencePicker = true }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Date
                SelectionRow(
                    label = if (uiState.entryType == BudgetEntryType.BUDGET) "Start Date" else "Date",
                    value = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(uiState.date),
                    icon = Icons.Rounded.CalendarToday,
                    onClick = { showDatePicker = true }
                )

                if (uiState.entryType == BudgetEntryType.BUDGET) {
                    Spacer(modifier = Modifier.height(24.dp))

                    // Alert Threshold
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Alert me at", style = MaterialTheme.typography.bodyMedium, color = SecondaryText)
                        Text(text = "${uiState.alertThresholdPercent}%", style = MaterialTheme.typography.bodyMedium, color = EmeraldGreen)
                    }
                    Slider(
                        value = uiState.alertThresholdPercent.toFloat(),
                        onValueChange = { viewModel.onAlertThresholdChange(it.toInt()) },
                        valueRange = 0f..100f,
                        colors = SliderDefaults.colors(
                            thumbColor = EmeraldGreen,
                            activeTrackColor = EmeraldGreen,
                            inactiveTrackColor = DividerColor
                        )
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Payment Method
                SelectionRow(
                    label = "Payment Method",
                    value = uiState.selectedPaymentMethod?.name ?: "Select Method",
                    icon = if (uiState.selectedPaymentMethod != null) PaymentMethodIcons.getIcon(uiState.selectedPaymentMethod!!.iconRes) else Icons.Rounded.AccountBalanceWallet,
                    iconColor = uiState.selectedPaymentMethod?.let { Color(android.graphics.Color.parseColor(it.colorHex)) } ?: MutedGold,
                    onClick = { showPaymentMethodPicker = true }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Note
                BudgetNoteSection(
                    note = uiState.note,
                    attachmentUri = uiState.attachmentUri,
                    onNoteChange = viewModel::onNoteChange,
                    onAttachmentClick = { showAttachmentOptions = true },
                    onRemoveAttachment = viewModel::onRemoveAttachment
                )
                
                Spacer(modifier = Modifier.height(32.dp))
            }

            // Keypad
            AnimatedVisibility(
                visible = uiState.isKeypadVisible,
                enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
                exit = slideOutVertically(targetOffsetY = { it }) + fadeOut()
            ) {
                NumericKeypad(
                    onNumberClick = viewModel::onAmountChange,
                    onDeleteClick = viewModel::onAmountDelete,
                    onDismiss = { viewModel.toggleKeypad() },
                    modifier = Modifier.background(AnalyticsCardSurface)
                )
            }
        }
    }

    // Category Sheets Logic (Single Bottom Sheet for everything Category-related)
    if (activeCategorySheet != null) {
        ModalBottomSheet(
            onDismissRequest = { activeCategorySheet = null },
            containerColor = DeepObsidian,
            dragHandle = null,
            shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)
        ) {
            when (val state = activeCategorySheet) {
                is CategorySheetState.Picker -> {
                    CategoryPickerContent(
                        viewModel = categoryViewModel,
                        onDismiss = { activeCategorySheet = null },
                        onNavigateToManagement = {},
                        onAddNewCategory = { parentId ->
                            activeCategorySheet = CategorySheetState.AddNew(parentId)
                        }
                    )
                    // Sync selected category back to viewmodel
                    LaunchedEffect(categoryViewModel.categorySelectedEvent) {
                        categoryViewModel.categorySelectedEvent.collect {
                            viewModel.onCategorySelected(it)
                            activeCategorySheet = null
                        }
                    }
                }
                is CategorySheetState.AddNew -> {
                    AddCategoryContent(
                        parentId = state.parentId,
                        onSave = { name, icon, color ->
                            categoryViewModel.addCategory(name, icon, color, state.parentId)
                            activeCategorySheet = CategorySheetState.Picker // Return to picker inside the same sheet
                        },
                        onDismiss = { 
                            activeCategorySheet = CategorySheetState.Picker // Return to picker
                        }
                    )
                }
                else -> {}
            }
        }
    }

    if (showPaymentMethodPicker) {
        ModalBottomSheet(
            onDismissRequest = { showPaymentMethodPicker = false },
            containerColor = DeepObsidian,
            dragHandle = null,
            shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)
        ) {
            PaymentMethodPickerContent(
                paymentMethods = paymentMethods,
                selectedMethod = uiState.selectedPaymentMethod,
                onMethodSelected = {
                    viewModel.onPaymentMethodSelected(it)
                    showPaymentMethodPicker = false
                },
                onAddCustom = { name, icon, color, type ->
                    viewModel.addCustomPaymentMethod(name, icon, color, type)
                },
                onDismiss = { showPaymentMethodPicker = false }
            )
        }
    }

    if (showDatePicker) {
        CustomDatePickerBottomSheet(
            initialDate = uiState.date,
            onDateSelected = viewModel::onDateSelected,
            onDismiss = { showDatePicker = false }
        )
    }

    if (showRecurrencePicker) {
        RecurrenceBottomSheet(
            selected = uiState.recurrence,
            onSelected = {
                viewModel.onRecurrenceSelected(it)
                showRecurrencePicker = false
            },
            onDismiss = { showRecurrencePicker = false }
        )
    }

    if (showAttachmentOptions) {
        AttachmentOptionsBottomSheet(
            onTakePhoto = {
                showAttachmentOptions = false
                showCamera = true
            },
            onChooseGallery = {
                showAttachmentOptions = false
                photoPickerLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            },
            onDismiss = { showAttachmentOptions = false }
        )
    }
}

@Composable
private fun AmountSection(
    amount: String,
    isKeypadVisible: Boolean,
    onToggleKeypad: () -> Unit,
    onKeypadOpen: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(AnalyticsBackground)
            .padding(bottom = 32.dp, top = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "HOW MUCH?",
            style = MaterialTheme.typography.labelMedium,
            color = SecondaryText,
            letterSpacing = 1.sp
        )
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickable(onClick = onKeypadOpen)
        ) {
            Text(
                text = "₹",
                style = MaterialTheme.typography.displayMedium.copy(color = Color(0xFFF5DFA0)),
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = formatAmount(amount),
                style = MaterialTheme.typography.displayLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = HighEmphasisText
                ),
                fontSize = 56.sp
            )
            Spacer(modifier = Modifier.width(8.dp))
            IconButton(onClick = onToggleKeypad) {
                Icon(
                    imageVector = if (isKeypadVisible) Icons.Rounded.KeyboardArrowDown else Icons.Rounded.KeyboardArrowUp,
                    contentDescription = "Toggle Keypad",
                    tint = SecondaryText
                )
            }
        }
    }
}

private fun formatAmount(amount: String): String {
    if (amount.isEmpty()) return "0"
    return try {
        val parts = amount.split(".")
        val formattedInt = parts[0].toLong().toString().reversed()
            .chunked(3).joinToString(",").reversed()
        if (parts.size > 1) "$formattedInt.${parts[1]}" else formattedInt
    } catch (e: Exception) {
        amount
    }
}

@Composable
private fun SelectionRow(
    label: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconColor: Color = MutedGold,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        color = AnalyticsCardSurface,
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(iconColor.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(imageVector = icon, contentDescription = null, tint = iconColor, modifier = Modifier.size(20.dp))
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(text = label, style = MaterialTheme.typography.labelSmall, color = SecondaryText)
                    Text(text = value, style = MaterialTheme.typography.bodyLarge, color = HighEmphasisText, fontWeight = FontWeight.SemiBold)
                }
            }
            Icon(Icons.Rounded.ChevronRight, contentDescription = null, tint = SecondaryText)
        }
    }
}

@Composable
private fun BudgetNoteSection(
    note: String,
    attachmentUri: Uri?,
    onNoteChange: (String) -> Unit,
    onAttachmentClick: () -> Unit,
    onRemoveAttachment: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = AnalyticsCardSurface,
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.AutoMirrored.Rounded.Notes, contentDescription = null, tint = MutedGold, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(12.dp))
                Text(text = "Note", style = MaterialTheme.typography.bodyLarge, color = HighEmphasisText, fontWeight = FontWeight.SemiBold)
            }
            
            TextField(
                value = note,
                onValueChange = onNoteChange,
                placeholder = { Text("Click to fill in the remarks", color = SecondaryText) },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = DividerColor,
                    unfocusedIndicatorColor = DividerColor,
                    focusedTextColor = HighEmphasisText,
                    unfocusedTextColor = HighEmphasisText
                ),
                trailingIcon = {
                    IconButton(onClick = onAttachmentClick) {
                        Icon(Icons.Rounded.CameraAlt, contentDescription = "Attach", tint = HighEmphasisText)
                    }
                }
            )

            if (attachmentUri != null) {
                Box(
                    modifier = Modifier
                        .padding(top = 12.dp)
                        .size(100.dp)
                        .clip(RoundedCornerShape(12.dp))
                ) {
                    AsyncImage(
                        model = attachmentUri,
                        contentDescription = "Attachment",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                    IconButton(
                        onClick = onRemoveAttachment,
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(4.dp)
                            .size(20.dp)
                            .background(Color.Black.copy(alpha = 0.5f), CircleShape)
                    ) {
                        Icon(Icons.Default.Close, contentDescription = "Remove", tint = Color.White, modifier = Modifier.size(12.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun BottomActionBar(
    onContinue: () -> Unit,
    onSave: () -> Unit,
    isSaveEnabled: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        OutlinedButton(
            onClick = onContinue,
            modifier = Modifier
                .weight(1f)
                .height(56.dp),
            shape = RoundedCornerShape(16.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, MutedGold),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = MutedGold)
        ) {
            Text("CONTINUE", fontWeight = FontWeight.Bold)
        }

        Button(
            onClick = onSave,
            modifier = Modifier
                .weight(1f)
                .height(56.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MutedGold,
                disabledContainerColor = MutedGold.copy(alpha = 0.5f)
            ),
            enabled = isSaveEnabled
        ) {
            Text("SAVE", color = DeepObsidian, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun RecurrenceBottomSheet(
    selected: RecurrenceType,
    onSelected: (RecurrenceType) -> Unit,
    onDismiss: () -> Unit
) {
    val options = listOf(
        RecurrenceType.NONE to "None (Default)",
        RecurrenceType.DAILY to "Everyday",
        RecurrenceType.WEEKEND to "weekends",
        RecurrenceType.MONTHLY to "Month Option"
    )

    GenericBottomSheet(
        title = "Period",
        onDismiss = onDismiss
    ) {
        Column(modifier = Modifier.padding(vertical = 12.dp)) {
            options.forEach { (type, label) ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onSelected(type) }
                        .padding(horizontal = 24.dp, vertical = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = label,
                        style = MaterialTheme.typography.bodyLarge,
                        color = HighEmphasisText,
                        fontWeight = if (selected == type) FontWeight.Bold else FontWeight.Normal,
                        modifier = Modifier.weight(1f)
                    )
                    if (selected == type) {
                        Icon(Icons.Rounded.Check, contentDescription = null, tint = EmeraldGreen)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AttachmentOptionsBottomSheet(
    onTakePhoto: () -> Unit,
    onChooseGallery: () -> Unit,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = DeepObsidian,
        dragHandle = null,
        shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)
    ) {
        Column(modifier = Modifier.padding(bottom = 24.dp)) {
            Text(
                text = "Attachment",
                style = MaterialTheme.typography.titleLarge,
                color = HighEmphasisText,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(24.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onTakePhoto)
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Rounded.Camera, contentDescription = null, tint = MutedGold)
                Spacer(modifier = Modifier.width(16.dp))
                Text(text = "Take Photo", style = MaterialTheme.typography.bodyLarge, color = HighEmphasisText)
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onChooseGallery)
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Rounded.PhotoLibrary, contentDescription = null, tint = MutedGold)
                Spacer(modifier = Modifier.width(16.dp))
                Text(text = "Choose from Gallery", style = MaterialTheme.typography.bodyLarge, color = HighEmphasisText)
            }
        }
    }
}

