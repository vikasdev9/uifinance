package com.uifinance.project291.ui.budget

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.uifinance.project291.data.local.entity.CategoryType as DataCategoryType
import com.uifinance.project291.design_system.*
import com.uifinance.project291.data.model.domain.RecurrenceType
import com.uifinance.project291.ui.budget.category.AddCategoryContent
import com.uifinance.project291.ui.budget.category.CategoryPickerContent
import com.uifinance.project291.ui.budget.category.CategoryPickerViewModel
import com.uifinance.project291.ui.budget.category.CategorySheetState
import com.uifinance.project291.ui.budget.components.*
import com.uifinance.project291.ui.budget.category.components.CategoryIcons
import com.uifinance.project291.ui.components.*
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTransactionScreen(
    onBack: () -> Unit,
    onSuccess: () -> Unit,
    onNavigateToCategoryManagement: () -> Unit,
    onNavigateToPaymentMethodManagement: () -> Unit,
    viewModel: AddTransactionViewModel = hiltViewModel(),
    categoryViewModel: CategoryPickerViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val tabs = listOf("Expense", "Income", "Transfer")
    
    var activeCategorySheet by remember { mutableStateOf<CategorySheetState?>(null) }
    var showDatePicker by remember { mutableStateOf(false) }
    var showPaymentMethodPicker by remember { mutableStateOf(false) }
    var isSelectingToWallet by remember { mutableStateOf(false) }
    var showRecurrencePicker by remember { mutableStateOf(false) }
    var showAttachmentOptions by remember { mutableStateOf(false) }
    var showCamera by remember { mutableStateOf(false) }
    var isKeypadVisible by remember { mutableStateOf(false) }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri -> uri?.let { viewModel.onAttachmentAdded(it) } }
    )

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
            TopAppBar(
                title = { Text(text = "Add ${tabs[uiState.selectedTab]}", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
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
        bottomBar = {
            Column(modifier = Modifier.background(DeepObsidian)) {
                AnimatedVisibility(
                    visible = isKeypadVisible,
                    enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
                    exit = slideOutVertically(targetOffsetY = { it }) + fadeOut()
                ) {
                    NumericKeypad(
                        onNumberClick = viewModel::onAmountChange,
                        onDeleteClick = viewModel::onAmountDelete,
                        onDismiss = { isKeypadVisible = false }
                    )
                }

                Button(
                    onClick = { viewModel.saveTransaction(onSuccess) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = EmeraldGreen,
                        disabledContainerColor = EmeraldGreen.copy(alpha = 0.5f)
                    ),
                    shape = RoundedCornerShape(16.dp),
                    enabled = uiState.isValid && !uiState.isSaving
                ) {
                    Text(
                        text = "SAVE ${tabs[uiState.selectedTab].uppercase(Locale.ROOT)}",
                        color = DeepObsidian,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            TabRow(
                selectedTabIndex = uiState.selectedTab,
                containerColor = DeepObsidian,
                contentColor = EmeraldGreen,
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        Modifier.tabIndicatorOffset(tabPositions[uiState.selectedTab]),
                        color = EmeraldGreen
                    )
                },
                divider = {}
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = uiState.selectedTab == index,
                        onClick = { 
                            viewModel.onTabSelected(index)
                            when (index) {
                                0 -> categoryViewModel.setCategoryType(DataCategoryType.EXPENSE)
                                1 -> categoryViewModel.setCategoryType(DataCategoryType.INCOME)
                            }
                        },
                        text = {
                            Text(
                                text = title,
                                color = if (uiState.selectedTab == index) EmeraldGreen else SecondaryText,
                                fontWeight = if (uiState.selectedTab == index) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    )
                }
            }

            Box(modifier = Modifier.weight(1f)) {
                if (uiState.selectedTab == 2) {
                    TransferView(
                        uiState = uiState,
                        onFromWalletClick = {
                            isSelectingToWallet = false
                            showPaymentMethodPicker = true
                        },
                        onToWalletClick = {
                            isSelectingToWallet = true
                            showPaymentMethodPicker = true
                        },
                        onDateClick = { showDatePicker = true },
                        onAmountClick = { isKeypadVisible = true },
                        onRecurrenceClick = { showRecurrencePicker = true },
                        onAttachmentClick = { showAttachmentOptions = true },
                        onSwapWallets = viewModel::onSwapWallets,
                        onNoteChange = viewModel::onNoteChange,
                        onRemoveAttachment = viewModel::onRemoveAttachment
                    )
                } else {
                    TransactionView(
                        uiState = uiState,
                        onCategoryClick = {
                            val type = if (uiState.selectedTab == 1) DataCategoryType.INCOME else DataCategoryType.EXPENSE
                            categoryViewModel.setCategoryType(type)
                            activeCategorySheet = CategorySheetState.Picker
                        },
                        onDateClick = { showDatePicker = true },
                        onAmountClick = { isKeypadVisible = true },
                        onPaymentMethodClick = {
                            isSelectingToWallet = false
                            showPaymentMethodPicker = true
                        },
                        onRecurrenceClick = { showRecurrencePicker = true },
                        onAttachmentClick = { showAttachmentOptions = true },
                        onNoteChange = viewModel::onNoteChange,
                        onRemoveAttachment = viewModel::onRemoveAttachment
                    )
                }
            }
        }
    }

    // Dialogs/BottomSheets
    if (showDatePicker) {
        CustomDatePickerBottomSheet(
            initialDate = uiState.date,
            onDateSelected = {
                viewModel.onDateChange(it)
                showDatePicker = false
            },
            onDismiss = { showDatePicker = false }
        )
    }

    AppModalBottomSheet(
        visible = activeCategorySheet != null,
        onDismissRequest = { activeCategorySheet = null },
        skipPartiallyExpanded = false
    ) {
        when (val state = activeCategorySheet) {
                is CategorySheetState.Picker -> {
                    CategoryPickerContent(
                        viewModel = categoryViewModel,
                        onDismiss = { activeCategorySheet = null },
                        onNavigateToManagement = {
                            activeCategorySheet = null
                            onNavigateToCategoryManagement()
                        },
                        onAddNewCategory = { parentId ->
                            activeCategorySheet = CategorySheetState.AddNew(parentId)
                        }
                    )
                    
                    LaunchedEffect(categoryViewModel.categorySelectedEvent) {
                        categoryViewModel.categorySelectedEvent.collect { category ->
                            // Convert back to domain model for VM
                            viewModel.onCategoryChange(com.uifinance.project291.data.model.domain.Category(
                                id = category.id,
                                name = category.name,
                                iconRes = category.iconRes,
                                colorHex = category.colorHex,
                                type = if (category.type == DataCategoryType.INCOME) com.uifinance.project291.data.model.domain.CategoryType.INCOME else com.uifinance.project291.data.model.domain.CategoryType.EXPENSE,
                                parentId = category.parentId
                            ))
                            activeCategorySheet = null
                        }
                    }
                }
                is CategorySheetState.AddNew -> {
                    AddCategoryContent(
                        parentId = state.parentId,
                        onSave = { name, icon, color ->
                            categoryViewModel.addCategory(name, icon, color, state.parentId)
                            activeCategorySheet = CategorySheetState.Picker
                        },
                        onDismiss = {
                            activeCategorySheet = CategorySheetState.Picker
                        }
                    )
                }
                else -> {}
            }
        }

    if (showPaymentMethodPicker) {
        val selectedPaymentMethod = if (uiState.selectedTab == 2) {
            if (isSelectingToWallet) uiState.toWallet else uiState.fromWallet
        } else uiState.selectedPaymentMethod

        // We need to map domain PaymentMethod to data PaymentMethod for existing picker
        val dataPaymentMethods = uiState.paymentMethods.map { 
            com.uifinance.project291.data.local.entity.PaymentMethod(
                id = it.id,
                name = it.name,
                iconRes = it.iconRes,
                colorHex = it.colorHex,
                type = if (it.type == com.uifinance.project291.data.model.domain.PaymentMethodType.ASSET) com.uifinance.project291.data.local.entity.PaymentMethodType.ASSET else com.uifinance.project291.data.local.entity.PaymentMethodType.LIABILITY,
                allowNegativeBalance = it.allowNegativeBalance,
                isActive = true
            )
        }
        val selectedDataMethod = selectedPaymentMethod?.let { domain ->
            dataPaymentMethods.find { it.id == domain.id }
        }

        AppModalBottomSheet(
            visible = showPaymentMethodPicker,
            onDismissRequest = { showPaymentMethodPicker = false }
        ) {
            PaymentMethodPickerContent(
                paymentMethods = dataPaymentMethods,
                selectedMethod = selectedDataMethod,
                onMethodSelected = { data ->
                    val domain = uiState.paymentMethods.find { it.id == data.id }
                    domain?.let {
                        if (uiState.selectedTab == 2) {
                            if (isSelectingToWallet) viewModel.onToWalletSelected(it)
                            else viewModel.onFromWalletSelected(it)
                        } else {
                            viewModel.onPaymentMethodSelected(it)
                        }
                    }
                    showPaymentMethodPicker = false
                },
                onAddCustom = { name, icon, color, type ->
                    // This could be moved to domain/usecase later
                },
                onNavigateToManagement = {
                    showPaymentMethodPicker = false
                    onNavigateToPaymentMethodManagement()
                },
                onDismiss = { showPaymentMethodPicker = false }
            )
        }
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
fun TransactionView(
    uiState: AddTransactionUiState,
    onCategoryClick: () -> Unit,
    onDateClick: () -> Unit,
    onAmountClick: () -> Unit,
    onPaymentMethodClick: () -> Unit,
    onRecurrenceClick: () -> Unit,
    onAttachmentClick: () -> Unit,
    onNoteChange: (String) -> Unit,
    onRemoveAttachment: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp)
    ) {
        Spacer(modifier = Modifier.height(24.dp))
        AmountInput(amount = uiState.amount, onAmountClick = onAmountClick)
        Spacer(modifier = Modifier.height(32.dp))

        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            SelectorRow(
                label = "Category",
                value = uiState.selectedCategory?.name ?: "Select Category",
                icon = if (uiState.selectedCategory != null) CategoryIcons.getIcon(uiState.selectedCategory.iconRes) else Icons.Rounded.Category,
                onClick = onCategoryClick
            )

            SelectorRow(
                label = "Payment Method",
                value = uiState.selectedPaymentMethod?.name ?: "Select Method",
                icon = Icons.Rounded.AccountBalanceWallet,
                onClick = onPaymentMethodClick
            )

            val dateStr = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(uiState.date)
            SelectorRow(label = "Date", value = dateStr, icon = Icons.Rounded.CalendarToday, onClick = onDateClick)

            SelectorRow(
                label = "Period",
                value = when(uiState.recurrence) {
                    RecurrenceType.NONE -> "NONE"
                    RecurrenceType.DAILY -> "EVERYDAY"
                    RecurrenceType.WEEKEND -> "WEEKENDS"
                    RecurrenceType.MONTHLY -> "MONTHLY"
                    else -> uiState.recurrence.name
                },
                icon = Icons.Rounded.Update,
                onClick = onRecurrenceClick
            )

            NoteInput(
                note = uiState.note,
                attachmentUri = uiState.attachmentUri,
                onNoteChange = onNoteChange,
                onAttachmentClick = onAttachmentClick,
                onRemoveAttachment = onRemoveAttachment
            )
        }
        Spacer(modifier = Modifier.height(40.dp))
    }
}

@Composable
fun TransferView(
    uiState: AddTransactionUiState,
    onFromWalletClick: () -> Unit,
    onToWalletClick: () -> Unit,
    onDateClick: () -> Unit,
    onAmountClick: () -> Unit,
    onRecurrenceClick: () -> Unit,
    onAttachmentClick: () -> Unit,
    onSwapWallets: () -> Unit,
    onNoteChange: (String) -> Unit,
    onRemoveAttachment: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp)
    ) {
        Spacer(modifier = Modifier.height(24.dp))
        AmountInput(amount = uiState.amount, onAmountClick = onAmountClick)
        Spacer(modifier = Modifier.height(32.dp))

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

            var rotationAngle by remember { mutableStateOf(0f) }
            val rotation by animateFloatAsState(
                targetValue = rotationAngle,
                animationSpec = spring(stiffness = Spring.StiffnessLow),
                label = "SwapRotation"
            )

            IconButton(
                onClick = { 
                    rotationAngle += 180f
                    onSwapWallets() 
                },
                modifier = Modifier
                    .align(Alignment.Center)
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

        if (uiState.isInsufficientBalance) {
            Text(
                text = "Insufficient balance in ${uiState.fromWallet?.name}",
                color = NegativeRed,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 8.dp, start = 16.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        val dateStr = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(uiState.date)
        SelectorRow(label = "Date", value = dateStr, icon = Icons.Rounded.CalendarToday, onClick = onDateClick)

        Spacer(modifier = Modifier.height(16.dp))

        SelectorRow(
            label = "Recurrence",
            value = uiState.recurrence.name.lowercase().replaceFirstChar { it.uppercase() },
            icon = Icons.Rounded.Update,
            onClick = onRecurrenceClick
        )

        Spacer(modifier = Modifier.height(16.dp))

        NoteInput(
            note = uiState.note,
            attachmentUri = uiState.attachmentUri,
            onNoteChange = onNoteChange,
            onAttachmentClick = onAttachmentClick,
            onRemoveAttachment = onRemoveAttachment
        )

        Spacer(modifier = Modifier.height(40.dp))
    }
}
