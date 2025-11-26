package com.example.simplescaffoldapp.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// 定义体彩主题色 - 红色系
private val LotteryRed = Color(0xFFE53935)
private val LotteryRedDark = Color(0xFFB71C1C)
private val LotteryGold = Color(0xFFFFD700)
private val LotteryBackground = Color(0xFFFFFBFE)

// 浅色主题配色
private val LightColorScheme = lightColorScheme(
    primary = LotteryRed,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFFFDAD6),
    onPrimaryContainer = LotteryRedDark,
    secondary = LotteryGold,
    onSecondary = Color.Black,
    background = LotteryBackground,
    surface = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F)
)

// 深色主题配色
private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFFFB4AB),
    onPrimary = Color(0xFF690005),
    primaryContainer = LotteryRedDark,
    onPrimaryContainer = Color(0xFFFFDAD6),
    secondary = LotteryGold,
    onSecondary = Color.Black,
    background = Color(0xFF1C1B1F),
    surface = Color(0xFF1C1B1F),
    onBackground = Color(0xFFE6E1E5),
    onSurface = Color(0xFFE6E1E5)
)

/**
 * 体育彩票应用主题
 */
@Composable
fun SimpleScaffoldAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}