package com.uifinance.project291.ui.category

import androidx.compose.animation.*
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
import com.uifinance.project291.ui.category.components.AddEditCategoryBottomSheet
import com.uifinance.project291.ui.category.components.CategoryIcons

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryManagementScreen(
    viewModel: CategoryManagementViewModel,
    onBackClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val categoryType by viewModel.categoryType.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()

    var showAddSheet by remember { mutableStateOf(false) }
    var selectedParentIdForAdd by remember { mutableStateOf<Long?>(null) }
    var categoryToEdit by remember { mutableStateOf<Category?>(null) }
    var categoryToDelete by remember { mutableStateOf<Category?>(null) }
    var isSearchActive by remember { mutableStateOf(false) }

    if (categoryToDelete != null) {
        val hasChildren = (uiState as? CategoryManagementUiState.Success)?.categories?.find { it.category.id == categoryToDelete?.id }?.children?.isNotEmpty() == true
        
        AlertDialog(
            onDismissRequest = { categoryToDelete = null },
            title = { Text("Delete Category?") },
            text = { 
                Column {
                    Text("Are you sure you want to delete '${categoryToDelete?.name}'?")
                    if (hasChildren) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("This category contains subcategories. What would you like to do?", style = MaterialTheme.typography.bodySmall, color = SecondaryText)
                    }
                }
            },
            confirmButton = {
                if (hasChildren) {
                    Column(horizontalAlignment = Alignment.End) {
                        TextButton(onClick = {
                            viewModel.deleteCategory(categoryToDelete!!, moveChildrenToOther = true)
                            categoryToDelete = null
                        }) {
                            Text("DELETE PARENT ONLY (Move children to Other)", color = EmeraldGreen)
                        }
                        TextButton(onClick = {
                            viewModel.deleteCategory(categoryToDelete!!, moveChildrenToOther = false)
                            categoryToDelete = null
                        }) {
                            Text("DELETE EVERYTHING", color = NegativeRed)
                        }
                    }
                } else {
                    TextButton(onClick = {
                        viewModel.deleteCategory(categoryToDelete!!, moveChildrenToOther = false)
                        categoryToDelete = null
                    }) {
                        Text("DELETE", color = NegativeRed)
                    }
                }
            },
            dismissButton = {
                TextButton(onClick = { categoryToDelete = null }) {
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
                title = {
                    if (isSearchActive) {
                        TextField(
                            value = searchQuery,
                            onValueChange = { viewModel.setSearchQuery(it) },
                            placeholder = { Text("Search categories...", color = SecondaryText) },
                            modifier = Modifier.fillMaxWidth(),
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                focusedTextColor = HighEmphasisText,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent
                            ),
                            singleLine = true
                        )
                    } else {
                        Text("Manage Categories", fontWeight = FontWeight.Bold)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = if (isSearchActive) { { isSearchActive = false; viewModel.setSearchQuery("") } } else onBackClick) {
                        Icon(if (isSearchActive) Icons.Rounded.Close else Icons.Rounded.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    if (!isSearchActive) {
                        IconButton(onClick = { isSearchActive = true }) {
                            Icon(Icons.Rounded.Search, contentDescription = "Search")
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = DeepObsidian,
                    titleContentColor = HighEmphasisText,
                    navigationIconContentColor = HighEmphasisText,
                    actionIconContentColor = HighEmphasisText
                )
            )
        },
        containerColor = DeepObsidian,
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    selectedParentIdForAdd = null
                    categoryToEdit = null
                    showAddSheet = true
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
                    if (state.categories.isEmpty()) {
                        EmptyState()
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            item {
                                CategorySummary(
                                    total = state.totalCount,
                                    parents = state.parentCount
                                )
                            }
                            
                            state.categories.forEach { categoryWithChildren ->
                                item(key = "parent_${categoryWithChildren.category.id}") {
                                    ParentCategoryRow(
                                        category = categoryWithChildren.category,
                                        onEdit = {
                                            categoryToEdit = it
                                            showAddSheet = true
                                        },
                                        onDelete = { categoryToDelete = it },
                                        onAddSub = {
                                            selectedParentIdForAdd = categoryWithChildren.category.id
                                            categoryToEdit = null
                                            showAddSheet = true
                                        }
                                    )
                                }
                                items(categoryWithChildren.children, key = { "child_${it.id}" }) { child ->
                                    ChildCategoryRow(
                                    category = child,
                                    onEdit = {
                                        categoryToEdit = it
                                        showAddSheet = true
                                    },
                                    onDelete = { categoryToDelete = it }
                                )
                                }
                                item {
                                    TextButton(
                                        onClick = {
                                            selectedParentIdForAdd = categoryWithChildren.category.id
                                            categoryToEdit = null
                                            showAddSheet = true
                                        },
                                        modifier = Modifier.padding(start = 32.dp)
                                    ) {
                                        Icon(Icons.Rounded.Add, contentDescription = null, modifier = Modifier.size(16.dp), tint = EmeraldGreen)
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("Add subcategory", color = EmeraldGreen, style = MaterialTheme.typography.labelLarge)
                                    }
                                }
                            }
                            item { Spacer(modifier = Modifier.height(80.dp)) }
                        }
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

    if (showAddSheet) {
        val parentCategories = (uiState as? CategoryManagementUiState.Success)?.categories?.map { it.category } ?: emptyList()
        ModalBottomSheet(
            onDismissRequest = { showAddSheet = false },
            containerColor = DeepObsidian,
            shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)
        ) {
            AddEditCategoryBottomSheet(
                category = categoryToEdit,
                parentId = selectedParentIdForAdd,
                parentCategories = parentCategories,
                onDismiss = { showAddSheet = false },
                onConfirm = { name, icon, color, parentId ->
                    if (categoryToEdit == null) {
                        viewModel.addCategory(name, icon, color, parentId)
                    } else {
                        viewModel.updateCategory(categoryToEdit!!.copy(name = name, iconRes = icon, colorHex = color, parentId = parentId))
                    }
                    showAddSheet = false
                }
            )
        }
    }
}

@Composable
fun CategorySummary(total: Int, parents: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(CardSurface.copy(alpha = 0.5f))
            .padding(12.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        SummaryItem(label = "Total", value = total.toString())
        SummaryItem(label = "Parents", value = parents.toString())
    }
}

@Composable
fun SummaryItem(label: String, value: String) {
    Column {
        Text(text = label, style = MaterialTheme.typography.labelSmall, color = SecondaryText)
        Text(text = value, style = MaterialTheme.typography.titleMedium, color = HighEmphasisText, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun EmptyState() {
    Column(
        modifier = Modifier.fillMaxSize().padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            Icons.Rounded.Category,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = SecondaryText.copy(alpha = 0.2f)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text("No categories yet", color = HighEmphasisText, style = MaterialTheme.typography.titleMedium)
        Text(
            "Create categories to organize your expenses and income.",
            color = SecondaryText,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            modifier = Modifier.padding(top = 8.dp)
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
