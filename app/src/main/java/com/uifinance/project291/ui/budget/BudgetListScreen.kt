package com.uifinance.project291.ui.budget

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Category
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.uifinance.project291.data.local.entity.Budget
import com.uifinance.project291.design_system.*
import com.uifinance.project291.ui.components.BudgetLinearProgress
import java.text.NumberFormat
import java.util.*

@Composable
fun BudgetListScreen(
    onAddBudget: () -> Unit,
    onEditBudget: (Long) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: BudgetViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(DeepObsidian)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Budgets",
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = HighEmphasisText
                )
            )
            Spacer(modifier = Modifier.height(24.dp))

            when (val state = uiState) {
                is BudgetUiState.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = EmeraldGreen)
                    }
                }
                is BudgetUiState.Success -> {
                    if (state.budgets.isEmpty()) {
                        EmptyBudgetsState()
                    } else {
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                            contentPadding = PaddingValues(bottom = 80.dp)
                        ) {
                            items(state.budgets) { budget ->
                                BudgetCard(
                                    budget = budget,
                                    onClick = { onEditBudget(budget.id) }
                                )
                            }
                        }
                    }
                }
                is BudgetUiState.Error -> {
                    Text(text = state.message, color = NegativeRed)
                }
            }
        }
    }
}

@Composable
fun BudgetCard(
    budget: Budget,
    onClick: () -> Unit
) {
    val currencyFormatter = rememberCurrencyFormatter()
    val spentPercent = (budget.amountSpent / budget.amountLimit * 100).toInt()

    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = CardSurface)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(IconCircleBackground),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Category,
                        contentDescription = null,
                        tint = EmeraldGreen,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = budget.name,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = HighEmphasisText
                    )
                    Text(
                        text = "${budget.period.name} Budget",
                        style = MaterialTheme.typography.bodySmall,
                        color = SecondaryText
                    )
                }
                Text(
                    text = "$spentPercent%",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = if (spentPercent > 100) NegativeRed else MutedGold
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
            BudgetLinearProgress(spent = budget.amountSpent, limit = budget.amountLimit)
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "${currencyFormatter.format(budget.amountSpent)} spent",
                    style = MaterialTheme.typography.bodySmall,
                    color = SecondaryText
                )
                Text(
                    text = "of ${currencyFormatter.format(budget.amountLimit)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = SecondaryText
                )
            }
        }
    }
}

@Composable
private fun EmptyBudgetsState() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "No active budgets",
            style = MaterialTheme.typography.titleLarge,
            color = SecondaryText
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Create your first budget to track spending",
            style = MaterialTheme.typography.bodyMedium,
            color = SecondaryText.copy(alpha = 0.7f)
        )
    }
}

@Composable
fun rememberCurrencyFormatter(): NumberFormat {
    return androidx.compose.runtime.remember {
        NumberFormat.getCurrencyInstance(Locale("en", "IN")).apply {
            maximumFractionDigits = 0
        }
    }
}
