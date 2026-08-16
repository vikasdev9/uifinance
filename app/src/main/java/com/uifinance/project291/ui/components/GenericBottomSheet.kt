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
import com.uifinance.project291.design_system.AnalyticsBackground
import com.uifinance.project291.design_system.DeepObsidian
import com.uifinance.project291.design_system.HighEmphasisText

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GenericBottomSheet(
    title: String,
    onDismiss: () -> Unit,
    headerAction: @Composable (RowScope.() -> Unit)? = null,
    containerColor: Color = DeepObsidian,
    headerColor: Color = AnalyticsBackground,
    content: @Composable ColumnScope.() -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = containerColor,
        dragHandle = null,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
    ) {
        GenericBottomSheetContent(
            title = title,
            onDismiss = onDismiss,
            headerAction = headerAction,
            headerColor = headerColor,
            content = content
        )
    }
}

@Composable
fun GenericBottomSheetContent(
    title: String,
    onDismiss: () -> Unit,
    headerAction: @Composable (RowScope.() -> Unit)? = null,
    headerColor: Color = AnalyticsBackground,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        // Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(headerColor)
                .padding(horizontal = 20.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                color = HighEmphasisText,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f)
            )
            
            headerAction?.invoke(this)
            
            IconButton(onClick = onDismiss) {
                Icon(Icons.Rounded.Close, contentDescription = "Close", tint = HighEmphasisText)
            }
        }
        
        // Content
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding())
        ) {
            content()
        }
    }
}
