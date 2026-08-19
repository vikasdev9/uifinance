package com.uifinance.project291.ui.budget

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.uifinance.project291.data.model.Budget
import com.uifinance.project291.data.model.BudgetUiState
import com.uifinance.project291.design_system.*
import com.uifinance.project291.ui.components.BudgetLinearProgress
import java.text.NumberFormat
import java.util.*

@Composable
fun BudgetListScreen(
    modifier: Modifier = Modifier,
    viewModel: BudgetListViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DeepObsidian)
            .padding(horizontal = 20.dp)
    ) {
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "Budgets",
            style = MaterialTheme.typography.headlineLarge.copy(
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold
            ),
            color = HighEmphasisText
        )
        Spacer(modifier = Modifier.height(24.dp))

        BudgetOverviewCard(
            totalBudgeted = uiState.totalBudgeted,
            totalSpent = uiState.totalSpent
        )

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Active Budgets",
                style = MaterialTheme.typography.titleMedium,
                color = HighEmphasisText
            )
            Text(
                text = "Edit",
                style = MaterialTheme.typography.labelMedium,
                color = EmeraldGreen
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(bottom = 100.dp)
        ) {
            items(uiState.budgets) { budget ->
                BudgetItem(budget = budget)
            }
        }
    }
}

@Composable
private fun BudgetOverviewCard(
    totalBudgeted: Double,
    totalSpent: Double
) {
    val currencyFormatter = remember {
        NumberFormat.getCurrencyInstance(Locale.US).apply {
            minimumFractionDigits = 0
            maximumFractionDigits = 0
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(CardSurface)
            .padding(24.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "Total Spent",
                        style = MaterialTheme.typography.labelSmall,
                        color = SecondaryText
                    )
                    Text(
                        text = currencyFormatter.format(totalSpent),
                        style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                        color = HighEmphasisText
                    )
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "Total Budget",
                        style = MaterialTheme.typography.labelSmall,
                        color = SecondaryText
                    )
                    Text(
                        text = currencyFormatter.format(totalBudgeted),
                        style = MaterialTheme.typography.titleMedium,
                        color = MutedGold
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            BudgetLinearProgress(
                spent = totalSpent,
                limit = totalBudgeted,
                modifier = Modifier.height(10.dp)
            )
        }
    }
}

@Composable
private fun BudgetItem(budget: Budget) {
    val currencyFormatter = remember {
        NumberFormat.getCurrencyInstance(Locale.US).apply {
            minimumFractionDigits = 0
            maximumFractionDigits = 0
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(CardSurface.copy(alpha = 0.5f))
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(budget.color.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = budget.categoryIcon,
                    contentDescription = null,
                    tint = budget.color,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = budget.categoryName,
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium),
                    color = HighEmphasisText
                )
                Text(
                    text = "${budget.remainingDays} days left",
                    style = MaterialTheme.typography.bodySmall,
                    color = SecondaryText
                )
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = currencyFormatter.format(budget.spentAmount),
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                    color = HighEmphasisText
                )
                Text(
                    text = "of ${currencyFormatter.format(budget.limitAmount)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = SecondaryText
                )
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        BudgetLinearProgress(
            spent = budget.spentAmount,
            limit = budget.limitAmount,
            modifier = Modifier.height(6.dp)
        )
    }
}

@androidx.compose.runtime.Composable
private fun <T> remember(calculation: () -> T): T = androidx.compose.runtime.remember(calculation = calculation)
