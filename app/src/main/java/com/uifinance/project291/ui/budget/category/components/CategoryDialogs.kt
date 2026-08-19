package com.uifinance.project291.ui.budget.category.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.uifinance.project291.data.local.entity.Category
import com.uifinance.project291.design_system.*

@Composable
fun AddEditCategoryDialog(
    category: Category? = null,
    parentId: Long? = null,
    onDismiss: () -> Unit,
    onConfirm: (name: String, iconName: String, colorHex: String) -> Unit
) {
    var name by remember { mutableStateOf(category?.name ?: "") }
    var selectedIconName by remember { mutableStateOf(category?.iconRes ?: "category") }
    var selectedColorHex by remember { mutableStateOf(category?.colorHex ?: "#10B981") }

    val categoryColors = listOf(
        "#10B981", // EmeraldGreen
        "#D4AF37", // MutedGold
        "#F7931A", // CryptoOrange
        "#3B82F6", // EquityBlue
        "#F87171", // NegativeRed
        "#8B5CF6", // Purple
        "#06B6D4", // Cyan
        "#EC4899", // Pink
        "#6366F1", // Indigo
        "#F59E0B"  // Amber
    )

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(24.dp),
            color = CardSurface
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Text(
                    text = if (category == null) {
                        if (parentId != null) "New Sub-Category" else "New Category"
                    } else "Edit Category",
                    style = MaterialTheme.typography.titleLarge,
                    color = HighEmphasisText
                )

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Name") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MutedGold,
                        unfocusedBorderColor = SecondaryText,
                        focusedLabelColor = MutedGold,
                        unfocusedLabelColor = SecondaryText,
                        focusedTextColor = HighEmphasisText,
                        unfocusedTextColor = HighEmphasisText
                    ),
                    singleLine = true
                )

                Text(
                    text = "Select Icon",
                    style = MaterialTheme.typography.labelLarge,
                    color = SecondaryText
                )

                LazyVerticalGrid(
                    columns = GridCells.Adaptive(48.dp),
                    modifier = Modifier.height(150.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(CategoryIcons.icons) { (iconName, icon) ->
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(
                                    if (selectedIconName == iconName) MutedGold.copy(alpha = 0.2f)
                                    else IconCircleBackground
                                )
                                .border(
                                    width = 2.dp,
                                    color = if (selectedIconName == iconName) MutedGold else Color.Transparent,
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .clickable { selectedIconName = iconName },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = icon,
                                contentDescription = null,
                                tint = if (selectedIconName == iconName) MutedGold else SecondaryText,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                }

                Text(
                    text = "Select Color",
                    style = MaterialTheme.typography.labelLarge,
                    color = SecondaryText
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    categoryColors.forEach { colorHex ->
                        val color = Color(android.graphics.Color.parseColor(colorHex))
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(color)
                                .clickable { selectedColorHex = colorHex },
                            contentAlignment = Alignment.Center
                        ) {
                            if (selectedColorHex == colorHex) {
                                Icon(
                                    imageVector = Icons.Rounded.Check,
                                    contentDescription = null,
                                    tint = DeepObsidian,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("CANCEL", color = SecondaryText)
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Button(
                        onClick = { onConfirm(name, selectedIconName, selectedColorHex) },
                        enabled = name.isNotBlank(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MutedGold,
                            contentColor = DeepObsidian
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("SAVE", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

