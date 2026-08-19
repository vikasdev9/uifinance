package com.uifinance.project291.ui.budget.category

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import com.uifinance.project291.design_system.*
import com.uifinance.project291.ui.budget.category.components.CategoryIcons
import com.uifinance.project291.ui.components.GenericBottomSheetContent

@Composable
fun AddCategoryContent(
    parentId: Long? = null,
    onSave: (name: String, icon: String, color: String) -> Unit,
    onDismiss: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var selectedIcon by remember { mutableStateOf("category") }
    var selectedColor by remember { mutableStateOf("#10B981") } // EmeraldGreen default
    var isIconGridExpanded by remember { mutableStateOf(true) }

    val colors = listOf("#10B981", "#D4AF37", "#3B82F6", "#F87171", "#F7931A", "#9C27B0", "#E91E63", "#00BCD4")

    GenericBottomSheetContent(
        title = if (parentId == null) "Add Parent Category" else "Add Subcategory",
        onDismiss = onDismiss
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            // Category Name Section
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Rounded.Description, contentDescription = null, tint = SecondaryText, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Category Name *",
                    style = MaterialTheme.typography.bodyMedium,
                    color = SecondaryText
                )
            }
            
            TextField(
                value = name,
                onValueChange = { name = it },
                placeholder = { Text("e.g. Salary", color = SecondaryText.copy(alpha = 0.5f)) },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = MutedGold,
                    unfocusedIndicatorColor = DividerColor,
                    focusedTextColor = HighEmphasisText,
                    unfocusedTextColor = HighEmphasisText
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Category Icon Section Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { isIconGridExpanded = !isIconGridExpanded },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Rounded.Category, contentDescription = null, tint = SecondaryText, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "Category Icon",
                        style = MaterialTheme.typography.bodyMedium,
                        color = SecondaryText
                    )
                }
                Icon(
                    imageVector = if (isIconGridExpanded) Icons.Rounded.KeyboardArrowUp else Icons.Rounded.KeyboardArrowDown,
                    contentDescription = null,
                    tint = SecondaryText
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Selected Icon Preview and Color Palette
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Selected Icon
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(android.graphics.Color.parseColor(selectedColor))),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = CategoryIcons.getIcon(selectedIcon),
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }

                // Color palette row
                Row(
                    modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    colors.forEach { colorStr ->
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .clip(CircleShape)
                                .background(Color(android.graphics.Color.parseColor(colorStr)))
                                .clickable { selectedColor = colorStr }
                                .padding(2.dp)
                        ) {
                            if (selectedColor == colorStr) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .clip(CircleShape)
                                        .background(Color.White.copy(alpha = 0.4f))
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Expandable Icon Grid
            AnimatedVisibility(
                visible = isIconGridExpanded,
                enter = expandVertically(),
                exit = shrinkVertically()
            ) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(280.dp),
                    color = AnalyticsCardSurface.copy(alpha = 0.5f),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(6),
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(CategoryIcons.icons) { (iconName, iconVector) ->
                            Box(
                                modifier = Modifier
                                    .aspectRatio(1f)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(
                                        if (selectedIcon == iconName) Color(android.graphics.Color.parseColor(selectedColor))
                                        else Color(android.graphics.Color.parseColor(selectedColor)).copy(alpha = 0.15f)
                                    )
                                    .clickable { selectedIcon = iconName },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = iconVector,
                                    contentDescription = null,
                                    tint = if (selectedIcon == iconName) Color.White else Color(android.graphics.Color.parseColor(selectedColor)),
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = { if (name.isNotBlank()) onSave(name, selectedIcon, selectedColor) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MutedGold,
                    disabledContainerColor = MutedGold.copy(alpha = 0.5f)
                ),
                shape = RoundedCornerShape(16.dp),
                enabled = name.isNotBlank()
            ) {
                Text("SAVE", color = DeepObsidian, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
            }
            
            Spacer(modifier = Modifier.height(WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()))
        }
    }
}
