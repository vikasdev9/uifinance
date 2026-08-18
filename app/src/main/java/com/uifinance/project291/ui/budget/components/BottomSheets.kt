package com.uifinance.project291.ui.budget.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Camera
import androidx.compose.material.icons.rounded.PhotoLibrary
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.uifinance.project291.design_system.*
import com.uifinance.project291.data.model.domain.RecurrenceType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecurrenceBottomSheet(
    selected: RecurrenceType,
    onSelected: (RecurrenceType) -> Unit,
    onDismiss: () -> Unit
) {
    val options = listOf(
        RecurrenceType.NONE to "None (Default)",
        RecurrenceType.DAILY to "Everyday",
        RecurrenceType.WEEKEND to "Weekends",
        RecurrenceType.WEEKLY to "Weekly",
        RecurrenceType.MONTHLY to "Monthly"
    )

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = DeepObsidian,
        dragHandle = null,
        shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)
    ) {
        Column(modifier = Modifier.padding(bottom = 24.dp)) {
            Text(
                text = "Period",
                style = MaterialTheme.typography.titleLarge,
                color = HighEmphasisText,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(24.dp)
            )

            options.forEach { (type, label) ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onSelected(type) }
                        .padding(horizontal = 24.dp, vertical = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = selected == type,
                        onClick = { onSelected(type) },
                        colors = RadioButtonDefaults.colors(
                            selectedColor = EmeraldGreen,
                            unselectedColor = SecondaryText
                        )
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = label,
                        style = MaterialTheme.typography.bodyLarge,
                        color = HighEmphasisText
                    )
                }
            }
        }
    }
}


