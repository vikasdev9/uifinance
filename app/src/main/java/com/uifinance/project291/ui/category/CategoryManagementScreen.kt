package com.uifinance.project291.ui.category

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
import androidx.compose.ui.unit.sp
import com.uifinance.project291.data.local.entity.Category
import com.uifinance.project291.data.local.entity.CategoryType
import com.uifinance.project291.data.local.entity.CategoryWithChildren
import com.uifinance.project291.design_system.*
import com.uifinance.project291.ui.category.components.AddEditCategoryDialog
import com.uifinance.project291.ui.category.components.CategoryIcons

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryManagementScreen(
    viewModel: CategoryManagementViewModel,
    onBackClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val categoryType by viewModel.categoryType.collectAsState()

    var showAddDialog by remember { mutableStateOf(false) }
    var selectedParentIdForAdd by remember { mutableStateOf<Long?>(null) }
    var categoryToEdit by remember { mutableStateOf<Category?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Manage Categories", fontWeight = FontWeight.Bold) },
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
                    selectedParentIdForAdd = null
                    categoryToEdit = null
                    showAddDialog = true
                },
                containerColor = EmeraldGreen,
                contentColor = DeepObsidian,
                shape = CircleShape
            ) {
                Icon(Icons.Rounded.Add, contentDescription = "Add Category")
            }
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            // Type Selector (Expense/Income)
            CategoryTypeSelector(
                selectedType = categoryType,
                onTypeSelected = { viewModel.setCategoryType(it) }
            )

            when (val state = uiState) {
                is CategoryManagementUiState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = MutedGold)
                    }
                }
                is CategoryManagementUiState.Success -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        state.categories.forEach { categoryWithChildren ->
                            item {
                                ParentCategoryRow(
                                    category = categoryWithChildren.category,
                                    onEdit = {
                                        categoryToEdit = it
                                        showAddDialog = true
                                    },
                                    onDelete = { viewModel.deleteCategory(it) },
                                    onAddSub = {
                                        selectedParentIdForAdd = categoryWithChildren.category.id
                                        categoryToEdit = null
                                        showAddDialog = true
                                    }
                                )
                            }
                            items(categoryWithChildren.children) { child ->
                                ChildCategoryRow(
                                    category = child,
                                    onEdit = {
                                        categoryToEdit = it
                                        showAddDialog = true
                                    },
                                    onDelete = { viewModel.deleteCategory(it) }
                                )
                            }
                        }
                        item { Spacer(modifier = Modifier.height(80.dp)) }
                    }
                }
                is CategoryManagementUiState.Error -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(text = state.message, color = NegativeRed)
                    }
                }
            }
        }
    }

    if (showAddDialog) {
        AddEditCategoryDialog(
            category = categoryToEdit,
            parentId = selectedParentIdForAdd,
            onDismiss = { showAddDialog = false },
            onConfirm = { name, icon, color ->
                if (categoryToEdit == null) {
                    viewModel.addCategory(name, icon, color, selectedParentIdForAdd)
                } else {
                    viewModel.updateCategory(categoryToEdit!!.copy(name = name, iconRes = icon, colorHex = color))
                }
                showAddDialog = false
            }
        )
    }
}

@Composable
fun CategoryTypeSelector(
    selectedType: CategoryType,
    onTypeSelected: (CategoryType) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(CardSurface)
            .padding(4.dp)
    ) {
        CategoryTypeButton(
            text = "Expense",
            isSelected = selectedType == CategoryType.EXPENSE,
            onClick = { onTypeSelected(CategoryType.EXPENSE) },
            modifier = Modifier.weight(1f)
        )
        CategoryTypeButton(
            text = "Income",
            isSelected = selectedType == CategoryType.INCOME,
            onClick = { onTypeSelected(CategoryType.INCOME) },
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun CategoryTypeButton(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(if (isSelected) MutedGold else Color.Transparent)
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = if (isSelected) DeepObsidian else SecondaryText,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
        )
    }
}

@Composable
fun ParentCategoryRow(
    category: Category,
    onEdit: (Category) -> Unit,
    onDelete: (Category) -> Unit,
    onAddSub: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(CardSurface)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(Color(android.graphics.Color.parseColor(category.colorHex)).copy(alpha = 0.2f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = CategoryIcons.getIcon(category.iconRes),
                contentDescription = null,
                tint = Color(android.graphics.Color.parseColor(category.colorHex)),
                modifier = Modifier.size(24.dp)
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = category.name,
            style = MaterialTheme.typography.titleMedium,
            color = HighEmphasisText,
            modifier = Modifier.weight(1f)
        )
        
        IconButton(onClick = onAddSub) {
            Icon(Icons.Rounded.Add, contentDescription = "Add Sub", tint = EmeraldGreen)
        }
        
        CategoryOptionsMenu(
            onEdit = { onEdit(category) },
            onDelete = { onDelete(category) }
        )
        
        Icon(
            Icons.Rounded.DragHandle,
            contentDescription = "Drag",
            tint = SecondaryText,
            modifier = Modifier.padding(horizontal = 8.dp)
        )
    }
}

@Composable
fun ChildCategoryRow(
    category: Category,
    onEdit: (Category) -> Unit,
    onDelete: (Category) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 32.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(CardSurface.copy(alpha = 0.6f))
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color(android.graphics.Color.parseColor(category.colorHex)).copy(alpha = 0.15f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = CategoryIcons.getIcon(category.iconRes),
                contentDescription = null,
                tint = Color(android.graphics.Color.parseColor(category.colorHex)),
                modifier = Modifier.size(18.dp)
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = category.name,
            style = MaterialTheme.typography.bodyLarge,
            color = HighEmphasisText,
            modifier = Modifier.weight(1f)
        )

        CategoryOptionsMenu(
            onEdit = { onEdit(category) },
            onDelete = { onDelete(category) }
        )

        Icon(
            Icons.Rounded.DragHandle,
            contentDescription = "Drag",
            tint = SecondaryText.copy(alpha = 0.5f),
            modifier = Modifier.padding(horizontal = 8.dp)
        )
    }
}

@Composable
fun CategoryOptionsMenu(
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        IconButton(onClick = { expanded = true }) {
            Icon(Icons.Rounded.MoreVert, contentDescription = "Options", tint = SecondaryText)
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(CardSurface)
        ) {
            DropdownMenuItem(
                text = { Text("Edit") },
                onClick = {
                    expanded = false
                    onEdit()
                },
                leadingIcon = { Icon(Icons.Rounded.Edit, contentDescription = null) }
            )
            DropdownMenuItem(
                text = { Text("Delete", color = NegativeRed) },
                onClick = {
                    expanded = false
                    onDelete()
                },
                leadingIcon = { Icon(Icons.Rounded.Delete, contentDescription = null, tint = NegativeRed) }
            )
        }
    }
}
