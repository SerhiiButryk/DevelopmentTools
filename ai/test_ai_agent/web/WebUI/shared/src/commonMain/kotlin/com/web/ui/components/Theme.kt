package com.web.ui.components

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF0096c7),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFEDE9FE),
    onPrimaryContainer = Color(0xFF4F46E5),
    secondary = Color(0xFF6366F1),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFEDE9FE),
    onSecondaryContainer = Color(0xFF4338CA),
    background = Color(0xFFF8FAFC),
    onBackground = Color(0xFF111827),
    surface = Color(0xFFEDE9FE),
    onSurface = Color(0xFF111827),
    surfaceVariant = Color(0xFFF3F4F6),
    onSurfaceVariant = Color(0xFF4B5563),
    inverseSurface = Color(0xFF111827),
    inverseOnSurface = Color.White,
    error = Color(0xFFB00020),
    onError = Color.White,
)

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF0096c7),
    onPrimary = Color(0xFF1D1B5B),
    primaryContainer = Color(0xFF3530A5),
    onPrimaryContainer = Color.White,
    secondary = Color(0xFFC7C2FF),
    onSecondary = Color(0xFF1D1B5B),
    secondaryContainer = Color(0xFF3530A5),
    onSecondaryContainer = Color.White,
    background = Color(0xFF121212),
    onBackground = Color(0xFFE5E7EB),
    surface = Color(0xFF1E1E1E),
    onSurface = Color(0xFFE5E7EB),
    surfaceVariant = Color(0xFF2C2C2C),
    onSurfaceVariant = Color(0xFF9CA3AF),
    inverseSurface = Color(0xFFE5E7EB),
    inverseOnSurface = Color(0xFF121212),
    error = Color(0xFFCF6679),
    onError = Color.Black
)

@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme,
        content = content
    )
}
