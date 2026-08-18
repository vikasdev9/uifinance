package com.uifinance.project291.ui.budget.components

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Notes
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.rounded.CameraAlt
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.uifinance.project291.design_system.*

@Composable
fun NoteInput(
    note: String,
    attachmentUri: Uri?,
    onNoteChange: (String) -> Unit,
    onAttachmentClick: () -> Unit,
    onRemoveAttachment: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = CardSurface,
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.AutoMirrored.Rounded.Notes,
                    contentDescription = null,
                    tint = EmeraldGreen,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Note",
                    style = MaterialTheme.typography.bodyLarge,
                    color = HighEmphasisText,
                    fontWeight = FontWeight.Bold
                )
            }
            
            TextField(
                value = note,
                onValueChange = onNoteChange,
                placeholder = { Text("Click to fill in the remarks", color = SecondaryText) },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = DividerColor,
                    unfocusedIndicatorColor = DividerColor,
                    focusedTextColor = HighEmphasisText,
                    unfocusedTextColor = HighEmphasisText
                ),
                trailingIcon = {
                    IconButton(onClick = onAttachmentClick) {
                        Icon(Icons.Rounded.CameraAlt, contentDescription = "Attach", tint = HighEmphasisText)
                    }
                }
            )

            if (attachmentUri != null) {
                Box(
                    modifier = Modifier
                        .padding(top = 12.dp)
                        .size(100.dp)
                        .clip(RoundedCornerShape(12.dp))
                ) {
                    AsyncImage(
                        model = attachmentUri,
                        contentDescription = "Attachment",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                    IconButton(
                        onClick = onRemoveAttachment,
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(4.dp)
                            .size(20.dp)
                            .background(Color.Black.copy(alpha = 0.5f), CircleShape)
                    ) {
                        Icon(
                            Icons.Default.Close,
                            contentDescription = "Remove",
                            tint = Color.White,
                            modifier = Modifier.size(12.dp)
                        )
                    }
                }
            }
        }
    }
}
