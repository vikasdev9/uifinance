package com.uifinance.project291.ui.category.components

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import com.uifinance.project291.design_system.*
import com.uifinance.project291.ui.components.GenericBottomSheetContent

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditCategoryBottomSheet(
    category: Category? = null,
    parentId: Long? = null,
    parentCategories: List<Category> = emptyList(),
    onDismiss: () -> Unit,
    onConfirm: (name: String, icon: String, color: String, parentId: Long?) -> Unit
) {
    var name by remember { mutableStateOf(category?.name ?: "") }
    var selectedIcon by remember { mutableStateOf(category?.iconRes ?: "category") }
    var selectedColor by remember { mutableStateOf(category?.colorHex ?: "#10B981") }
    var selectedParentId by remember { mutableStateOf(category?.parentId ?: parentId) }
    var isParentCategory by remember { mutableStateOf(selectedParentId == null) }
    var showParentSelection by remember { mutableStateOf(false) }

    val colors = listOf("#10B981", "#D4AF37", "#3B82F6", "#F87171", "#F7931A", "#9C27B0", "#E91E63", "#00BCD4")

    GenericBottomSheetContent(
        title = if (category == null) "Add Category" else "Edit Category",
        onDismiss = onDismiss
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Category Name
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Category Name") },
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

            // Category Type Selection
            Column {
                Text("Category Type", style = MaterialTheme.typography.labelMedium, color = SecondaryText)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    TypeButton(
                        text = "Parent",
                        isSelected = isParentCategory,
                        onClick = { 
                            isParentCategory = true
                            selectedParentId = null
                        },
                        modifier = Modifier.weight(1f)
                    )
                    TypeButton(
                        text = "Child",
                        isSelected = !isParentCategory,
                        onClick = { isParentCategory = false },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // Parent Category Selection (if Child)
            AnimatedVisibility(visible = !isParentCategory) {
                Column {
                    Text("Parent Category", style = MaterialTheme.typography.labelMedium, color = SecondaryText)
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(CardSurface)
                            .clickable { showParentSelection = true }
                            .padding(16.dp)
                    ) {
                        val parent = parentCategories.find { it.id == selectedParentId }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            if (parent != null) {
                                Icon(
                                    imageVector = CategoryIcons.getIcon(parent.iconRes),
                                    contentDescription = null,
                                    tint = Color(android.graphics.Color.parseColor(parent.colorHex)),
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(parent.name, color = HighEmphasisText)
                            } else {
                                Text("Select Parent", color = SecondaryText)
                            }
                            Spacer(modifier = Modifier.weight(1f))
                            Icon(Icons.Rounded.ArrowDropDown, contentDescription = null, tint = SecondaryText)
                        }
                    }
                }
            }

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
                            imageVector = CategoryIcons.getIcon(selectedIcon),
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
                    .height(200.dp),
                color = CardSurface,
                shape = RoundedCornerShape(16.dp)
            ) {
                LazyVerticalGrid(
                    columns = GridCells.Adaptive(48.dp),
                    contentPadding = PaddingValues(12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(CategoryIcons.icons) { (iconName, iconVector) ->
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
                onClick = { onConfirm(name, selectedIcon, selectedColor, selectedParentId) },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = EmeraldGreen),
                shape = RoundedCornerShape(16.dp),
                enabled = name.isNotBlank() && (isParentCategory || selectedParentId != null)
            ) {
                Text("SAVE CATEGORY", color = DeepObsidian, fontWeight = FontWeight.Bold)
            }
            
            Spacer(modifier = Modifier.height(WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()))
        }
    }

    if (showParentSelection) {
        ModalBottomSheet(
            onDismissRequest = { showParentSelection = false },
            containerColor = DeepObsidian
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxWidth().padding(16.dp)
            ) {
                item {
                    Text("Select Parent Category", style = MaterialTheme.typography.titleLarge, color = HighEmphasisText, modifier = Modifier.padding(bottom = 16.dp))
                }
                items(parentCategories) { parent ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { 
                                selectedParentId = parent.id
                                showParentSelection = false
                            }
                            .padding(vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = CategoryIcons.getIcon(parent.iconRes),
                            contentDescription = null,
                            tint = Color(android.graphics.Color.parseColor(parent.colorHex)),
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(parent.name, color = HighEmphasisText)
                        if (selectedParentId == parent.id) {
                            Spacer(modifier = Modifier.weight(1f))
                            Icon(Icons.Rounded.Check, contentDescription = null, tint = EmeraldGreen)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TypeButton(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(if (isSelected) MutedGold else CardSurface)
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = if (isSelected) DeepObsidian else SecondaryText,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
        )
    }
}
