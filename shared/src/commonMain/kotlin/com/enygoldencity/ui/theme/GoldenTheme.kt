package com.enygoldencity.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val GoldPrimary = Color(0xFFC9A86A)
private val GoldOnPrimary = Color(0xFF1A1A1A)
private val GoldContainer = Color(0xFFFBF3E0)
private val NavyPrimary = Color(0xFF0E1A2B)
private val NavySecondary = Color(0xFF1E3A5F)
private val Background = Color(0xFFFAF8F3)
private val Surface = Color(0xFFFFFFFF)
private val OnSurface = Color(0xFF1A1A1A)

private val GoldenColorScheme = lightColorScheme(
    primary = GoldPrimary,
    onPrimary = GoldOnPrimary,
    primaryContainer = GoldContainer,
    onPrimaryContainer = NavyPrimary,
    secondary = NavyPrimary,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFE8EEF6),
    background = Background,
    onBackground = OnSurface,
    surface = Surface,
    onSurface = OnSurface,
    surfaceVariant = Color(0xFFF0EBDC),
    onSurfaceVariant = Color(0xFF5A4A2F),
    outline = Color(0xFFE9DDC0),
    outlineVariant = Color(0xFFF0EBDC),
    error = Color(0xFFBA1A1A)
)

@Composable
fun GoldenCityTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = GoldenColorScheme,
        content = content
    )
}

// Responsive helpers
enum class WindowSize { Compact, Medium, Expanded }

@Composable
fun rememberWindowSize(): WindowSize {
    // Use BoxWithConstraints pattern via Local density? Simple fallback for web: rely on caller BoxWithConstraints
    return WindowSize.Expanded
}
