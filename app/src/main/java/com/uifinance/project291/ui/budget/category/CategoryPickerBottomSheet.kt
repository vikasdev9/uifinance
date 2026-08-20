package com.uifinance.project291.ui.budget.category

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.uifinance.project291.design_system.*
import com.uifinance.project291.ui.budget.category.components.CategoryIcons

@Composable
fun CategoryPickerContent(
    viewModel: CategoryPickerViewModel,
    onNavigateToManagement: () -> Unit,
    onDismiss: () -> Unit,
    onAddNewCategory: (parentId: Long?) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val selectedParent by viewModel.selectedParentCategory.collectAsState()
    val selectedChild by viewModel.selectedChildCategory.collectAsState()

    // Auto-select first parent if none selected
    LaunchedEffect(uiState) {
        val state = uiState
        if (state is CategoryPickerUiState.Success && selectedParent == null && state.categories.isNotEmpty()) {
            viewModel.selectParentCategory(state.categories.first().category)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 400.dp, max = 800.dp) // Set a reasonable range instead of fixed 90%
            .padding(bottom = 24.dp)
    ) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = onNavigateToManagement) {
                Icon(Icons.Rounded.Settings, contentDescription = "Manage", tint = SecondaryText)
            }
            Text(
                text = "Category",
                style = MaterialTheme.typography.titleLarge,
                color = HighEmphasisText,
                fontWeight = FontWeight.Bold
            )
            IconButton(onClick = onDismiss) {
                Icon(Icons.Rounded.Close, contentDescription = "Close", tint = SecondaryText)
            }
        }

        HorizontalDivider(color = DividerColor, thickness = 0.5.dp)

        when (val state = uiState) {
                is CategoryPickerUiState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = MutedGold)
                    }
                }
                is CategoryPickerUiState.Success -> {
                    Row(modifier = Modifier.fillMaxSize()) {
                        // Left Pane: Parents (40%)
                        Column(
                            modifier = Modifier
                                .weight(0.4f)
                                .background(CardSurface.copy(alpha = 0.5f))
                                .fillMaxHeight()
                        ) {
                            LazyColumn(modifier = Modifier.weight(1f)) {
                                items(state.categories) { categoryWithChildren ->
                                    ParentCategoryItem(
                                        category = categoryWithChildren.category,
                                        isSelected = selectedParent?.id == categoryWithChildren.category.id,
                                        onClick = { viewModel.selectParentCategory(categoryWithChildren.category) }
                                    )
                                }
                            }
                            
                            // Add New Parent button
                            Box(modifier = Modifier.padding(12.dp)) {
                                AddNewButton(
                                    onClick = { onAddNewCategory(null) },
                                    compact = true
                                )
                            }
                        }

                        // Right Pane: Children (60%)
                        Column(
                            modifier = Modifier
                                .weight(0.6f)
                                .fillMaxHeight()
                                .background(DeepObsidian)
                        ) {
                            val selectedCategoryWithChildren = state.categories.find { it.category.id == selectedParent?.id }
                            val children = selectedCategoryWithChildren?.children ?: emptyList()
                            
                            Column(modifier = Modifier.fillMaxSize()) {
                                Column(modifier = Modifier.weight(1f)) {
                                    if (selectedParent != null) {
                                        LazyColumn(
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .padding(horizontal = 16.dp)
                                        ) {
                                            // Show Parent itself as a selectable option
                                            item {
                                                ChildCategoryItem(
                                                    category = selectedParent!!,
                                                    isSelected = selectedChild?.id == selectedParent!!.id,
                                                    onClick = { viewModel.selectChildCategory(selectedParent!!) }
                                                )
                                                HorizontalDivider(color = DividerColor.copy(alpha = 0.3f), modifier = Modifier.padding(vertical = 4.dp))
                                            }

                                            items(children) { child ->
                                                ChildCategoryItem(
                                                    category = child,
                                                    isSelected = selectedChild?.id == child.id,
                                                    onClick = { viewModel.selectChildCategory(child) }
                                                )
                                            }
                                        }
                                    } else {
                                        // Empty state for right pane
                                        Box(
                                            modifier = Modifier.fillMaxSize(),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = "Select a category on the left",
                                                color = SecondaryText,
                                                style = MaterialTheme.typography.bodyMedium
                                            )
                                        }
                                    }
                                }
                                
                                // ADD NEW button at the bottom of right pane (aligned as in image)
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    contentAlignment = Alignment.CenterEnd
                                ) {
                                    Surface(
                                        onClick = { onAddNewCategory(selectedParent?.id) },
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
                                            Text(text = "ADD NEW", color = MutedGold, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                is CategoryPickerUiState.Error -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(text = state.message, color = NegativeRed)
                    }
                }
            }
        }
    }

@Composable
fun ParentCategoryItem(
    category: Category,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .background(if (isSelected) DeepObsidian else Color.Transparent)
            .padding(vertical = 16.dp, horizontal = 12.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(android.graphics.Color.parseColor(category.colorHex))),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = CategoryIcons.getIcon(category.iconRes),
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = category.name,
                style = MaterialTheme.typography.bodyMedium,
                color = if (isSelected) MutedGold else HighEmphasisText,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
            )
        }
    }
}

@Composable
fun ChildCategoryItem(
    category: Category,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        color = if (isSelected) MutedGold.copy(alpha = 0.1f) else CardSurface,
        border = if (isSelected) BorderStroke(1.dp, MutedGold) else null
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(android.graphics.Color.parseColor(category.colorHex))),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = CategoryIcons.getIcon(category.iconRes),
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(22.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = category.name,
                    style = MaterialTheme.typography.bodyLarge,
                    color = HighEmphasisText
                )
            }
            if (isSelected) {
                Icon(
                    imageVector = Icons.Rounded.Check,
                    contentDescription = null,
                    tint = MutedGold,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
fun AddNewButton(onClick: () -> Unit, compact: Boolean = false) {
    Surface(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = Color.Transparent,
        border = BorderStroke(1.dp, MutedGold.copy(alpha = 0.5f))
    ) {
        Row(
            modifier = Modifier
                .padding(vertical = if (compact) 8.dp else 12.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Rounded.Add, 
                contentDescription = null, 
                tint = MutedGold,
                modifier = Modifier.size(if (compact) 16.dp else 18.dp)
            )
            Spacer(modifier = Modifier.width(if (compact) 4.dp else 8.dp))
            Text(
                text = "ADD NEW", 
                color = MutedGold,
                fontWeight = FontWeight.Bold, 
                letterSpacing = 1.sp,
                fontSize = if (compact) 10.sp else 12.sp
            )
        }
    }
}
