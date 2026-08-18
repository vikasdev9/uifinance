package com.uifinance.project291.ui.budget.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Camera
import androidx.compose.material.icons.rounded.PhotoLibrary
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.uifinance.project291.design_system.DeepObsidian
import com.uifinance.project291.design_system.HighEmphasisText
import com.uifinance.project291.design_system.MutedGold

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