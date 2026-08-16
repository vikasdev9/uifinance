package com.uifinance.project291.ui.budget

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.uifinance.project291.design_system.*
import com.uifinance.project291.ui.category.CategoryPickerContent
import com.uifinance.project291.ui.category.CategoryPickerViewModel
import com.uifinance.project291.ui.category.AddCategoryContent
import com.uifinance.project291.ui.category.CategorySheetState
import com.uifinance.project291.ui.components.NumericKeypad
import com.uifinance.project291.ui.components.CustomDatePickerBottomSheet
import com.uifinance.project291.ui.payment.PaymentMethodPickerContent
import com.uifinance.project291.ui.transaction.TransactionViewModel
import com.uifinance.project291.ui.transaction.AddTransferViewModel
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.uifinance.project291.data.local.entity.CategoryType
import com.uifinance.project291.ui.components.CameraScreen
import androidx.compose.animation.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEntryScreen(
    onBack: () -> Unit,
    onSuccess: () -> Unit,
    onNavigateToCategoryManagement: () -> Unit,
    onNavigateToPaymentMethodManagement: () -> Unit,
    transactionViewModel: TransactionViewModel = hiltViewModel(),
    transferViewModel: AddTransferViewModel = hiltViewModel(),
    categoryViewModel: CategoryPickerViewModel = hiltViewModel()
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Expense", "Income", "Transfer")
    
    // Consolidated Category Sheet State
    var activeCategorySheet by remember { mutableStateOf<CategorySheetState?>(null) }

    var showDatePicker by remember { mutableStateOf(false) }
    var showPaymentMethodPicker by remember { mutableStateOf(false) }
    var isSelectingToWallet by remember { mutableStateOf(false) }
    var showRecurrencePicker by remember { mutableStateOf(false) }
    var showAttachmentOptions by remember { mutableStateOf(false) }
    var showCamera by remember { mutableStateOf(false) }
    var isKeypadVisible by remember { mutableStateOf(true) }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri -> 
            uri?.let { 
                when (selectedTab) {
                    0, 1 -> transactionViewModel.onAttachmentAdded(it)
                    2 -> transferViewModel.onAttachmentAdded(it)
                }
            } 
        }
    )

    if (showCamera) {
        CameraScreen(
            onImageCaptured = { uri ->
                when (selectedTab) {
                    0, 1 -> transactionViewModel.onAttachmentAdded(uri)
                    2 -> transferViewModel.onAttachmentAdded(uri)
                }
                showCamera = false
            },
            onClose = { showCamera = false }
        )
        return
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Add ${tabs[selectedTab]}", fontWeight = FontWeight.Bold) },
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
                        onNumberClick = { 
                            when (selectedTab) {
                                0, 1 -> transactionViewModel.onAmountChange(it)
                                2 -> transferViewModel.onAmountChange(it)
                            }
                        },
                        onDeleteClick = {
                            when (selectedTab) {
                                0, 1 -> transactionViewModel.onAmountDelete()
                                2 -> transferViewModel.onAmountDelete()
                            }
                        },
                        onDismiss = { isKeypadVisible = false }
                    )
                }
                
                val transferUiState by transferViewModel.uiState.collectAsStateWithLifecycle()
                val isButtonEnabled = when (selectedTab) {
                    0, 1 -> true // Basic validation usually done in VM save
                    2 -> transferUiState.isValid
                    else -> true
                }

                Button(
                    onClick = { 
                        when (selectedTab) {
                            0 -> transactionViewModel.saveTransaction(CategoryType.EXPENSE, onSuccess)
                            1 -> transactionViewModel.saveTransaction(CategoryType.INCOME, onSuccess)
                            2 -> transferViewModel.saveTransfer(onSuccess)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = EmeraldGreen,
                        disabledContainerColor = EmeraldGreen.copy(alpha = 0.5f)
                    ),
                    shape = RoundedCornerShape(16.dp),
                    enabled = isButtonEnabled
                ) {
                    Text(text = "SAVE ${tabs[selectedTab].uppercase(java.util.Locale.ROOT)}", color = DeepObsidian, fontWeight = FontWeight.Bold)
                }
            }
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = DeepObsidian,
                contentColor = EmeraldGreen,
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                        color = EmeraldGreen
                    )
                },
                divider = {}
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { 
                            selectedTab = index 
                            // Refresh category list for income/expense
                            when (index) {
                                0 -> categoryViewModel.setCategoryType(CategoryType.EXPENSE)
                                1 -> categoryViewModel.setCategoryType(CategoryType.INCOME)
                            }
                        },
                        text = {
                            Text(
                                text = title,
                                color = if (selectedTab == index) EmeraldGreen else SecondaryText,
                                fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    )
                }
            }

            Box(modifier = Modifier.weight(1f)) {
                when (selectedTab) {
                    0, 1 -> {
                        TransactionEntryTab(
                            viewModel = transactionViewModel,
                            onCategoryClick = {
                                val type = if (selectedTab == 1) CategoryType.INCOME else CategoryType.EXPENSE
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
                            onAttachmentClick = { showAttachmentOptions = true }
                        )
                    }
                    2 -> {
                        TransferTab(
                            viewModel = transferViewModel,
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
                            onAttachmentClick = { showAttachmentOptions = true }
                        )
                    }
                }
            }
        }
    }

    if (showDatePicker) {
        val initialDate = when (selectedTab) {
            2 -> transferViewModel.uiState.collectAsState().value.date
            else -> transactionViewModel.date.collectAsState().value
        }
        CustomDatePickerBottomSheet(
            initialDate = initialDate,
            onDateSelected = {
                when (selectedTab) {
                    2 -> transferViewModel.onDateChange(it)
                    else -> transactionViewModel.onDateChange(it)
                }
                showDatePicker = false
            },
            onDismiss = { showDatePicker = false }
        )
    }

    // Category Sheets Logic (Same as before, not used by Transfer)
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
                            when (selectedTab) {
                                0, 1 -> transactionViewModel.onCategoryChange(category)
                            }
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
    }

    if (showPaymentMethodPicker) {
        val selectedPaymentMethodId = when (selectedTab) {
            2 -> if (isSelectingToWallet) transferViewModel.uiState.collectAsState().value.toWallet?.id ?: 0 
                 else transferViewModel.uiState.collectAsState().value.fromWallet?.id ?: 0
            else -> transactionViewModel.selectedPaymentMethod.collectAsState().value?.id ?: 0
        }
        val paymentMethods by transactionViewModel.paymentMethods.collectAsStateWithLifecycle()
        
        ModalBottomSheet(
            onDismissRequest = { showPaymentMethodPicker = false },
            containerColor = DeepObsidian,
            dragHandle = null,
            shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)
        ) {
            PaymentMethodPickerContent(
                paymentMethods = paymentMethods,
                selectedMethod = paymentMethods.find { it.id == selectedPaymentMethodId },
                onMethodSelected = {
                    when (selectedTab) {
                        2 -> {
                            if (isSelectingToWallet) transferViewModel.onToWalletSelected(it)
                            else transferViewModel.onFromWalletSelected(it)
                        }
                        else -> transactionViewModel.onPaymentMethodSelected(it)
                    }
                    showPaymentMethodPicker = false
                },
                onAddCustom = { name, icon, color, type ->
                    when (selectedTab) {
                        2 -> { /* Transfer VM doesn't have add custom PM yet, can reuse transaction VM or add to it */ }
                        else -> transactionViewModel.addCustomPaymentMethod(name, icon, color, type)
                    }
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
        val recurrence = when (selectedTab) {
            2 -> transferViewModel.uiState.collectAsState().value.recurrence
            else -> transactionViewModel.recurrence.collectAsState().value
        }
        RecurrenceBottomSheet(
            selected = recurrence,
            onSelected = {
                when (selectedTab) {
                    2 -> transferViewModel.onRecurrenceSelected(it)
                    else -> transactionViewModel.onRecurrenceSelected(it)
                }
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
private fun PlaceholderTab(title: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center) {
        Text(text = "$title Entry Form", color = SecondaryText)
    }
}
