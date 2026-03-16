package com.bintang.myprofileapp.ui.theme

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = Color(0xFF7C3AED),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFEDE9FE),
    onPrimaryContainer = Color(0xFF1E0A4E),
    secondary = Color(0xFF0D9488),
    onSecondary = Color.White,
    background = Color(0xFFF8F9FA),
    onBackground = Color(0xFF1E2030),
    surface = Color.White,
    onSurface = Color(0xFF1E2030),
    surfaceVariant = Color(0xFFF1F3F5),
    onSurfaceVariant = Color(0xFF6B7280),
    outline = Color(0xFFD1D5DB)
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFFA78BFA),
    onPrimary = Color(0xFF1E0A4E),
    primaryContainer = Color(0xFF3B1D8E),
    onPrimaryContainer = Color(0xFFEDE9FE),
    secondary = Color(0xFF2DD4BF),
    onSecondary = Color(0xFF003D36),
    background = Color(0xFF0F1117),
    onBackground = Color(0xFFE5E7EB),
    surface = Color(0xFF1A1C25),
    onSurface = Color(0xFFE5E7EB),
    surfaceVariant = Color(0xFF252836),
    onSurfaceVariant = Color(0xFF9CA3AF),
    outline = Color(0xFF4B5563)
)

@Composable
fun AppTheme(
    isDarkMode: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (isDarkMode) DarkColors else LightColors

    val animatedBackground = animateColorAsState(
        targetValue = colorScheme.background,
        animationSpec = tween(durationMillis = 400),
        label = "bg"
    )
    val animatedSurface = animateColorAsState(
        targetValue = colorScheme.surface,
        animationSpec = tween(durationMillis = 400),
        label = "surface"
    )

    val smoothScheme = colorScheme.copy(
        background = animatedBackground.value,
        surface = animatedSurface.value
    )

    MaterialTheme(
        colorScheme = smoothScheme,
        content = content
    )
}
