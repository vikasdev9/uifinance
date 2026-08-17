package com.uifinance.project291.ui.budget


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.uifinance.project291.data.local.entity.RecurrenceType
import com.uifinance.project291.design_system.*
import com.uifinance.project291.ui.components.*


@Composable
fun RecurrenceBottomSheet(
    selected: RecurrenceType,
    onSelected: (RecurrenceType) -> Unit,
    onDismiss: () -> Unit
) {
    val options = listOf(
        RecurrenceType.NONE to "None (Default)",
        RecurrenceType.DAILY to "Everyday",
        RecurrenceType.WEEKEND to "weekends",
        RecurrenceType.MONTHLY to "Month Option"
    )

    GenericBottomSheet(
        title = "Period",
        onDismiss = onDismiss
    ) {
        Column(modifier = Modifier.padding(vertical = 12.dp)) {
            options.forEach { (type, label) ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onSelected(type) }
                        .padding(horizontal = 24.dp, vertical = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = label,
                        style = MaterialTheme.typography.bodyLarge,
                        color = HighEmphasisText,
                        fontWeight = if (selected == type) FontWeight.Bold else FontWeight.Normal,
                        modifier = Modifier.weight(1f)
                    )
                    if (selected == type) {
                        Icon(Icons.Rounded.Check, contentDescription = null, tint = EmeraldGreen)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AttachmentOptionsBottomSheet(
    onTakePhoto: () -> Unit,
    onChooseGallery: () -> Unit,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = DeepObsidian,
        dragHandle = null,
        shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)
    ) {
        Column(modifier = Modifier.padding(bottom = 24.dp)) {
            Text(
                text = "Attachment",
                style = MaterialTheme.typography.titleLarge,
                color = HighEmphasisText,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(24.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onTakePhoto)
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Rounded.Camera, contentDescription = null, tint = MutedGold)
                Spacer(modifier = Modifier.width(16.dp))
                Text(text = "Take Photo", style = MaterialTheme.typography.bodyLarge, color = HighEmphasisText)
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onChooseGallery)
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Rounded.PhotoLibrary, contentDescription = null, tint = MutedGold)
                Spacer(modifier = Modifier.width(16.dp))
                Text(text = "Choose from Gallery", style = MaterialTheme.typography.bodyLarge, color = HighEmphasisText)
            }
        }
    }
}

