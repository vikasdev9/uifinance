package com.uifinance.project291.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.uifinance.project291.design_system.DeepObsidian
import com.uifinance.project291.design_system.HighEmphasisText

/**
 * A centralized, production-ready Modal Bottom Sheet component for the application.
 * Standardizes styling (colors, shapes, behavior) and provides a clean API.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppModalBottomSheet(
    visible: Boolean,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
    title: String? = null,
    showCloseButton: Boolean = false,
    headerAction: @Composable (RowScope.() -> Unit)? = null,
    containerColor: Color = DeepObsidian,
    headerColor: Color = Color.Transparent,
    skipPartiallyExpanded: Boolean = true,
    sheetState: SheetState = rememberModalBottomSheetState(skipPartiallyExpanded = skipPartiallyExpanded),
    content: @Composable ColumnScope.() -> Unit,
) {
    if (visible) {
        ModalBottomSheet(
            onDismissRequest = onDismissRequest,
            modifier = modifier,
            sheetState = sheetState,
            containerColor = containerColor,
            dragHandle = null,
            shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
            content = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .navigationBarsPadding() // Ensures content is above navigation bar
                ) {
                    if ((title != null) || showCloseButton || (headerAction != null)) {
                        BottomSheetHeader(
                            title = title,
                            onDismiss = onDismissRequest,
                            showCloseButton = showCloseButton,
                            headerAction = headerAction,
                            headerColor = headerColor
                        )
                    }
                    content()
                }
            }
        )
    }
}

@Composable
private fun BottomSheetHeader(
    title: String?,
    onDismiss: () -> Unit,
    showCloseButton: Boolean,
    headerAction: @Composable (RowScope.() -> Unit)?,
    headerColor: Color
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(headerColor)
            .padding(horizontal = 20.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (title != null) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                color = HighEmphasisText,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )
        } else {
            Spacer(modifier = Modifier.weight(1f))
        }

        headerAction?.invoke(this)

        if (showCloseButton) {
            IconButton(onClick = onDismiss) {
                Icon(
                    imageVector = Icons.Rounded.Close,
                    contentDescription = "Close",
                    tint = HighEmphasisText
                )
            }
        }
    }
}
