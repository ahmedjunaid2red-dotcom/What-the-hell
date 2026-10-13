package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val NoirColorScheme = darkColorScheme(
    primary = NeonCyan,
    onPrimary = TextDark,
    primaryContainer = NoirSurfaceElevated,
    onPrimaryContainer = NeonCyan,
    secondary = NeonMagenta,
    onSecondary = TextDark,
    secondaryContainer = NoirSurfaceElevated,
    onSecondaryContainer = NeonMagenta,
    tertiary = PoliceTapeAmber,
    onTertiary = TextDark,
    background = NoirBackground,
    onBackground = PaperWhite,
    surface = NoirSurface,
    onSurface = PaperWhite,
    surfaceVariant = NoirSurfaceElevated,
    onSurfaceVariant = TextMuted,
    outline = NoirSurfaceBorder,
    error = AlertCrimson
)

@Composable
fun EchoesOfMindTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = NoirColorScheme,
        typography = Typography,
        content = content
    )
}
