package com.uifinance.project291.ui.budget

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Notes
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.uifinance.project291.data.local.entity.RecurrenceType
import com.uifinance.project291.design_system.*
import com.uifinance.project291.ui.category.components.CategoryIcons
import com.uifinance.project291.ui.transaction.TransactionViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun TransactionEntryTab(
    viewModel: TransactionViewModel,
    onCategoryClick: () -> Unit,
    onDateClick: () -> Unit,
    onAmountClick: () -> Unit,
    onPaymentMethodClick: () -> Unit,
    onRecurrenceClick: () -> Unit,
    onAttachmentClick: () -> Unit
) {
    val amount by viewModel.amount.collectAsStateWithLifecycle()
    val selectedCategory by viewModel.selectedCategory.collectAsStateWithLifecycle()
    val selectedPaymentMethod by viewModel.selectedPaymentMethod.collectAsStateWithLifecycle()
    val date by viewModel.date.collectAsStateWithLifecycle()
    val recurrence by viewModel.recurrence.collectAsStateWithLifecycle()
    val note by viewModel.note.collectAsStateWithLifecycle()
    val attachmentUri by viewModel.attachmentUri.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp)
    ) {
        Spacer(modifier = Modifier.height(24.dp))
        
        Box(modifier = Modifier.clickable(onClick = onAmountClick)) {
            TransactionAmountDisplay(amount = amount)
        }
        
        Spacer(modifier = Modifier.height(32.dp))

        TransactionFormFields(
            selectedCategory = selectedCategory,
            onCategoryClick = onCategoryClick,
            selectedPaymentMethodName = selectedPaymentMethod?.name ?: "Select Method",
            onPaymentMethodClick = onPaymentMethodClick,
            date = date,
            onDateClick = onDateClick,
            recurrence = recurrence,
            onRecurrenceClick = onRecurrenceClick,
            note = note,
            onNoteChange = viewModel::onNoteChange,
            attachmentUri = attachmentUri,
            onAttachmentClick = onAttachmentClick,
            onRemoveAttachment = viewModel::onRemoveAttachment
        )

        Spacer(modifier = Modifier.height(40.dp))
    }
}

@Composable
private fun TransactionAmountDisplay(amount: String) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "HOW MUCH?",
            style = MaterialTheme.typography.labelSmall,
            color = SecondaryText
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "₹",
                style = MaterialTheme.typography.displayLarge.copy(color = MutedGold),
                fontSize = 40.sp
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = amount.ifEmpty { "0" },
                style = MaterialTheme.typography.displayLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = HighEmphasisText
                )
            )
        }
    }
}

@Composable
private fun TransactionFormFields(
    selectedCategory: com.uifinance.project291.data.local.entity.Category?,
    onCategoryClick: () -> Unit,
    selectedPaymentMethodName: String,
    onPaymentMethodClick: () -> Unit,
    date: Date,
    onDateClick: () -> Unit,
    recurrence: RecurrenceType,
    onRecurrenceClick: () -> Unit,
    note: String,
    onNoteChange: (String) -> Unit,
    attachmentUri: Uri?,
    onAttachmentClick: () -> Unit,
    onRemoveAttachment: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        // Category Selector
        SelectorRow(
            label = "Category",
            value = selectedCategory?.name ?: "Select Category",
            icon = if (selectedCategory != null) CategoryIcons.getIcon(selectedCategory.iconRes) else Icons.Rounded.Category,
            onClick = onCategoryClick
        )

        // Payment Method
        SelectorRow(
            label = "Payment Method",
            value = selectedPaymentMethodName,
            icon = Icons.Rounded.AccountBalanceWallet,
            onClick = onPaymentMethodClick
        )

        // Date
        val dateStr = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(date)
        SelectorRow(label = "Date", value = dateStr, icon = Icons.Rounded.CalendarToday, onClick = onDateClick)

        // Recurrence (Period)
        SelectorRow(
            label = "Period",
            value = when(recurrence) {
                RecurrenceType.NONE -> "NONE"
                RecurrenceType.DAILY -> "EVERYDAY"
                RecurrenceType.WEEKEND -> "WEEKENDS"
                RecurrenceType.MONTHLY -> "MONTHLY"
                else -> recurrence.name
            },
            icon = Icons.Rounded.Update,
            onClick = onRecurrenceClick
        )

        // Note Section
        NoteSection(
            note = note,
            attachmentUri = attachmentUri,
            onNoteChange = onNoteChange,
            onAttachmentClick = onAttachmentClick,
            onRemoveAttachment = onRemoveAttachment
        )
    }
}

@Composable
internal fun NoteSection(
    note: String,
    attachmentUri: Uri?,
    onNoteChange: (String) -> Unit,
    onAttachmentClick: () -> Unit,
    onRemoveAttachment: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = CardSurface,
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.AutoMirrored.Rounded.Notes, contentDescription = null, tint = EmeraldGreen, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(12.dp))
                Text(text = "Note", style = MaterialTheme.typography.bodyLarge, color = HighEmphasisText, fontWeight = FontWeight.Bold)
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
                        Icon(Icons.Default.Close, contentDescription = "Remove", tint = Color.White, modifier = Modifier.size(12.dp))
                    }
                }
            }
        }
    }
}
