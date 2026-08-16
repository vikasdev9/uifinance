package com.uifinance.project291.ui.budget

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import com.uifinance.project291.design_system.DeepObsidian
import com.uifinance.project291.design_system.EmeraldGreen
import com.uifinance.project291.design_system.HighEmphasisText
import com.uifinance.project291.design_system.SecondaryText
import com.uifinance.project291.design_system.NovaVestTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEntryScreen(
    onBack: () -> Unit,
    onSuccess: () -> Unit
) {
    var selectedTab by remember { mutableIntStateOf(0) } // Default to Budget tab
    val tabs = listOf("Budget", "Income", "Expense")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Add ${tabs[selectedTab]}", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = DeepObsidian,
                    titleContentColor = HighEmphasisText,
                    navigationIconContentColor = HighEmphasisText
                )
            )
        },
        containerColor = DeepObsidian
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = DeepObsidian,
                contentColor = EmeraldGreen,
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                        color = EmeraldGreen
                    )
                },
                divider = {}
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = {
                            Text(
                                text = title,
                                color = if (selectedTab == index) EmeraldGreen else SecondaryText,
                                fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    )
                }
            }

            when (selectedTab) {
                0 -> BudgetEntryTab(onSaveSuccess = onSuccess)
                1 -> PlaceholderTab(title = "Income")
                2 -> PlaceholderTab(title = "Expense")
            }
        }
    }
}

@Composable
private fun PlaceholderTab(title: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center) {
        Text(text = "$title Entry Form", color = SecondaryText)
    }
}
