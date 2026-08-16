package com.uifinance.project291.ui.budget

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Category
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.uifinance.project291.data.local.entity.BudgetPeriod
import com.uifinance.project291.data.local.entity.Recurrence
import com.uifinance.project291.design_system.*
import com.uifinance.project291.ui.components.NumericKeypad
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun BudgetEntryTab(
    onSaveSuccess: () -> Unit,
    viewModel: AddEditBudgetViewModel = hiltViewModel()
) {
    val amount by viewModel.amount.collectAsStateWithLifecycle()
    val name by viewModel.name.collectAsStateWithLifecycle()
    val period by viewModel.period.collectAsStateWithLifecycle()
    val startDate by viewModel.startDate.collectAsStateWithLifecycle()
    val recurrence by viewModel.recurrence.collectAsStateWithLifecycle()
    val alertThreshold by viewModel.alertThreshold.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp)
    ) {
        Spacer(modifier = Modifier.height(24.dp))
        
        // Amount Display
        AmountDisplay(amount = amount)
        
        Spacer(modifier = Modifier.height(32.dp))

        // Form Fields
        BudgetFormFields(
            name = name,
            onNameChange = viewModel::onNameChange,
            period = period,
            onPeriodChange = viewModel::onPeriodChange,
            startDate = startDate,
            recurrence = recurrence,
            onRecurrenceChange = viewModel::onRecurrenceChange,
            alertThreshold = alertThreshold,
            onThresholdChange = viewModel::onAlertThresholdChange
        )

        Spacer(modifier = Modifier.height(32.dp))

        NumericKeypad(
            onNumberClick = viewModel::onAmountChange,
            onDeleteClick = viewModel::onAmountDelete
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { viewModel.saveBudget(onSaveSuccess) },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(containerColor = EmeraldGreen),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text(text = "SAVE BUDGET", color = DeepObsidian, fontWeight = FontWeight.Bold)
        }
        
        Spacer(modifier = Modifier.height(40.dp))
    }
}

@Composable
private fun AmountDisplay(amount: String) {
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
private fun BudgetFormFields(
    name: String,
    onNameChange: (String) -> Unit,
    period: BudgetPeriod,
    onPeriodChange: (BudgetPeriod) -> Unit,
    startDate: Date,
    recurrence: Recurrence,
    onRecurrenceChange: (Recurrence) -> Unit,
    alertThreshold: Int,
    onThresholdChange: (Int) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        // Name
        OutlinedTextField(
            value = name,
            onValueChange = onNameChange,
            label = { Text("Budget Name") },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedTextColor = HighEmphasisText,
                focusedTextColor = HighEmphasisText,
                unfocusedBorderColor = DividerColor,
                focusedBorderColor = EmeraldGreen
            )
        )

        // Category Selector (Mocked for now)
        SelectorRow(label = "Category", value = "Food & Drinks", icon = Icons.Default.Category)

        // Period
        SelectorRow(label = "Period", value = period.name, onClick = { /* Show dropdown */ })

        // Start Date
        val dateStr = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(startDate)
        SelectorRow(label = "Start Date", value = dateStr, icon = Icons.Default.CalendarMonth)

        // Alert Threshold
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Alert me at", style = MaterialTheme.typography.bodyMedium, color = SecondaryText)
                Text(text = "$alertThreshold%", style = MaterialTheme.typography.bodyMedium, color = EmeraldGreen)
            }
            Slider(
                value = alertThreshold.toFloat(),
                onValueChange = { onThresholdChange(it.toInt()) },
                valueRange = 50f..100f,
                colors = SliderDefaults.colors(
                    thumbColor = EmeraldGreen,
                    activeTrackColor = EmeraldGreen,
                    inactiveTrackColor = DividerColor
                )
            )
        }
    }
}

@Composable
private fun SelectorRow(
    label: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector? = null,
    onClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(CardSurface)
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            if (icon != null) {
                Icon(imageVector = icon, contentDescription = null, tint = EmeraldGreen, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(12.dp))
            }
            Text(text = label, style = MaterialTheme.typography.bodyMedium, color = SecondaryText)
        }
        Text(text = value, style = MaterialTheme.typography.bodyMedium, color = HighEmphasisText, fontWeight = FontWeight.SemiBold)
    }
}
