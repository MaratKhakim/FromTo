package io.fromto.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val LightColorScheme = lightColorScheme(
    primary = Color(0xFF6200EE),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFBB86FC),
    onPrimaryContainer = Color(0xFF3700B3),

    secondary = Color(0xFF018786),
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFF018786),
    onSecondaryContainer = Color(0xFFFFFFFF),

    background = Color(0xFFFFFFFF),
    onBackground = Color(0xFF000000),

    surface = Color(0xFFF0F0F0),
    onSurface = Color(0xFF121212),
    surfaceVariant = Color(0xFFF0F0F0),
    onSurfaceVariant = Color(0xFF121212),

    error = Color(0xFFB00020),
    onError = Color(0xFFFFFFFF),
)

val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFBB86FC),
    onPrimary = Color(0xFF3700B3),
    primaryContainer = Color(0xFF6200EE),
    onPrimaryContainer = Color(0xFFFFFFFF),

    secondary = Color(0xFF03DAC6),
    onSecondary = Color(0xFF000000),
    secondaryContainer = Color(0xFF018786),
    onSecondaryContainer = Color(0xFFFFFFFF),

    background = Color(0xFF121212),
    onBackground = Color(0xFFD5D5D5),

    surface = Color(0xFF2D2D2D),
    onSurface = Color(0xFFE1E1E1),
    surfaceVariant = Color(0xFF2A2A2A),
    onSurfaceVariant = Color(0xFFFFFFFF),

    error = Color(0xFFCF6679),
    onError = Color(0xFF000000),
)

@Composable
fun TranslatorTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}
