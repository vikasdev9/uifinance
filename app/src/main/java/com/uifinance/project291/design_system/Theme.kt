package com.uifinance.project291.design_system

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val NovaVestColorScheme = darkColorScheme(
    primary = EmeraldGreen,
    onPrimary = DeepObsidian,
    secondary = MutedGold,
    onSecondary = DeepObsidian,
    tertiary = RealEstateGreen,
    background = DeepObsidian,
    onBackground = HighEmphasisText,
    surface = CardSurface,
    onSurface = HighEmphasisText,
    surfaceVariant = IconCircleBackground,
    onSurfaceVariant = SecondaryText,
    error = NegativeRed,
    onError = HighEmphasisText,
)

@Composable
fun NovaVestTheme(content: @Composable () -> Unit) {
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window ?: return@SideEffect
            window.statusBarColor = DeepObsidian.toArgb()
            window.navigationBarColor = BottomNavBackground.toArgb()
            WindowCompat.getInsetsController(window, view).apply {
                isAppearanceLightStatusBars = false
                isAppearanceLightNavigationBars = false
            }
        }
    }

    MaterialTheme(
        colorScheme = NovaVestColorScheme,
        typography = NovaVestTypography,
        content = content,
    )
}
