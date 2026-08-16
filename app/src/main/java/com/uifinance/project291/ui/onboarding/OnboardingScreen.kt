package com.uifinance.project291.ui.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import com.uifinance.project291.design_system.*
import kotlinx.coroutines.launch

@Composable
fun OnboardingScreen(
    onFinish: () -> Unit
) {
    val pagerState = rememberPagerState(pageCount = { 3 })
    val scope = rememberCoroutineScope()

    Scaffold(
        containerColor = DeepObsidian,
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(horizontal = 24.dp, vertical = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Page Indicator
                Row(
                    Modifier
                        .height(8.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    repeat(3) { iteration ->
                        val color = if (pagerState.currentPage == iteration) EmeraldGreen else DividerColor
                        Box(
                            modifier = Modifier
                                .padding(horizontal = 4.dp)
                                .clip(CircleShape)
                                .background(color)
                                .size(if (pagerState.currentPage == iteration) 24.dp else 8.dp, 8.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Action Button
                val buttonText = when (pagerState.currentPage) {
                    0 -> "Join NovaVest Now"
                    1 -> "Get Started"
                    else -> "Create My Account"
                }

                Button(
                    onClick = {
                        if (pagerState.currentPage < 2) {
                            scope.launch { pagerState.animateScrollToPage(pagerState.currentPage + 1) }
                        } else {
                            onFinish()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = EmeraldGreen,
                        contentColor = DeepObsidian
                    ),
                    shape = RoundedCornerShape(28.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = buttonText,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null)
                    }
                }
            }
        }
    ) { padding ->
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) { page ->
            when (page) {
                0 -> OnboardingPage(
                    title = "Effortless\nBank\nSimplified",
                    description = "Experience seamless financial management makes managing your finances easy and intuitive",
                    illustration = { CardIllustration() }
                )
                1 -> OnboardingPage(
                    title = "Take Control\nwith Smart Budgets",
                    description = "Set budgets, track spending, and stay on top of your goals with real-time insights.",
                    illustration = { BudgetIllustration() }
                )
                2 -> OnboardingPage(
                    title = "Smarter Decisions\nfor a Brighter Future",
                    description = "Understand your money, build better habits, and achieve your financial dreams.",
                    illustration = { InsightsIllustration() }
                )
            }
        }
    }
}

@Composable
private fun OnboardingPage(
    title: String,
    description: String,
    illustration: @Composable () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            illustration()
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = title,
            style = MaterialTheme.typography.displayMedium.copy(
                lineHeight = 48.sp,
                fontWeight = FontWeight.Bold
            ),
            color = HighEmphasisText
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = description,
            style = MaterialTheme.typography.bodyLarge,
            color = SecondaryText,
            modifier = Modifier.fillMaxWidth(0.9f)
        )
        
        Spacer(modifier = Modifier.height(40.dp))
    }
}

@Composable
private fun CardIllustration() {
    Box(modifier = Modifier.size(280.dp), contentAlignment = Alignment.Center) {
        // Decorative background glow
        Box(
            modifier = Modifier
                .size(200.dp)
                .background(
                    Brush.radialGradient(
                        colors = listOf(EmeraldGreen.copy(alpha = 0.2f), Color.Transparent)
                    )
                )
        )

        // Stacked Cards
        repeat(3) { i ->
            val rotation = (i - 1) * 10f
            val offset = (i - 1) * 20
            Card(
                modifier = Modifier
                    .size(200.dp, 120.dp)
                    .graphicsLayer { rotationZ = rotation }
                    .offset(x = offset.dp, y = (offset / 2).dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (i == 2) EmeraldGreen else CardSurface.copy(alpha = 0.8f)
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Box(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                    Icon(
                        Icons.Default.AccountBalanceWallet,
                        contentDescription = null,
                        tint = if (i == 2) DeepObsidian else EmeraldGreen,
                        modifier = Modifier.align(Alignment.TopStart)
                    )
                    Text(
                        "**** **** **** 1234",
                        color = if (i == 2) DeepObsidian else HighEmphasisText,
                        modifier = Modifier.align(Alignment.BottomStart),
                        style = MaterialTheme.typography.labelSmall
                    )
                }
            }
        }
    }
}

@Composable
private fun BudgetIllustration() {
    Box(modifier = Modifier.size(280.dp), contentAlignment = Alignment.Center) {
        // Central circle
        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .background(EmeraldGreen.copy(alpha = 0.1f))
                .background(
                    Brush.verticalGradient(
                        listOf(EmeraldGreen.copy(alpha = 0.3f), Color.Transparent)
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                Icons.Default.PieChart,
                contentDescription = null,
                tint = EmeraldGreen,
                modifier = Modifier.size(64.dp)
            )
        }

        // Surrounding mini-widgets
        repeat(3) { i ->
            val angle = i * 120f
            Box(
                modifier = Modifier
                    .offset(
                        x = (Math.cos(Math.toRadians(angle.toDouble())) * 90).dp,
                        y = (Math.sin(Math.toRadians(angle.toDouble())) * 90).dp
                    )
                    .size(60.dp, 40.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(CardSurface),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.6f)
                        .height(4.dp)
                        .clip(CircleShape)
                        .background(DividerColor)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.7f)
                            .fillMaxHeight()
                            .background(EmeraldGreen)
                    )
                }
            }
        }
    }
}

@Composable
private fun InsightsIllustration() {
    Box(modifier = Modifier.size(280.dp), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                Icons.AutoMirrored.Filled.TrendingUp,
                contentDescription = null,
                tint = EmeraldGreen,
                modifier = Modifier.size(80.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            // Simplified Bar Chart
            Row(
                modifier = Modifier.height(100.dp),
                verticalAlignment = Alignment.Bottom,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf(0.4f, 0.7f, 0.5f, 0.9f, 0.6f).forEach { height ->
                    Box(
                        modifier = Modifier
                            .width(16.dp)
                            .fillMaxHeight(height)
                            .clip(RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp))
                            .background(
                                Brush.verticalGradient(
                                    listOf(EmeraldGreen, EmeraldGreen.copy(alpha = 0.3f))
                                )
                            )
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun OnboardingScreenPreview() {
    NovaVestTheme {
        OnboardingScreen(onFinish = {})
    }
}
