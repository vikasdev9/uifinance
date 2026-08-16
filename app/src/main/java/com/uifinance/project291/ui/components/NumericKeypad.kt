package com.uifinance.project291.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.automirrored.filled.Backspace
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardHide
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.uifinance.project291.design_system.HighEmphasisText

@Composable
fun NumericKeypad(
    onNumberClick: (String) -> Unit,
    onDeleteClick: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val keys = listOf(
        listOf("7", "8", "9", "delete"),
        listOf("4", "5", "6", "+"),
        listOf("1", "2", "3", "-"),
        listOf("00", "0", ".", "dismiss")
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White)
    ) {
        keys.forEachIndexed { rowIndex, row ->
            if (rowIndex == 0) {
                HorizontalDivider(color = Color(0xFFEEEEEE))
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                row.forEachIndexed { colIndex, key ->
                    KeyItem(
                        key = key,
                        modifier = Modifier.weight(1f),
                        onClick = {
                            when (key) {
                                "delete" -> onDeleteClick()
                                "dismiss" -> onDismiss()
                                "+" -> { /* Operation logic if needed */ }
                                "-" -> { /* Operation logic if needed */ }
                                else -> onNumberClick(key)
                            }
                        }
                    )
                    if (colIndex < row.size - 1) {
                        VerticalDivider(modifier = Modifier.fillMaxHeight().width(1.dp), color = Color(0xFFEEEEEE))
                    }
                }
            }
            HorizontalDivider(color = Color(0xFFEEEEEE))
        }
    }
}

@Composable
private fun KeyItem(
    key: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxHeight()
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        when (key) {
            "delete" -> {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Backspace,
                    contentDescription = "Delete",
                    tint = Color(0xFF333333),
                    modifier = Modifier.size(24.dp)
                )
            }
            "dismiss" -> {
                Icon(
                    imageVector = Icons.Default.KeyboardHide,
                    contentDescription = "Dismiss",
                    tint = Color(0xFF333333),
                    modifier = Modifier.size(24.dp)
                )
            }
            else -> {
                Text(
                    text = key,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Medium,
                        fontSize = 20.sp
                    ),
                    color = Color(0xFF333333)
                )
            }
        }
    }
}
