package com.example.vktestappvideoplayer.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFFF4545), // Ярко-красный для акцента
    onPrimary = Color.Black,
    secondary = Color(0xFF616161), // Светло-серый
    onSecondary = Color.White,
    background = Color(0xFF121212), // Темный фон
    onBackground = Color.White,
    surface = Color(0xFF1E1E1E), // Темная поверхность
    onSurface = Color.White,
    error = Color(0xFFCF6679), // Красный для ошибок
    onError = Color.Black
)
private val LightColorScheme = lightColorScheme(
    primary = Color(0xFFFF0000), // Красный (YouTube)
    onPrimary = Color.White,
    secondary = Color(0xFF282828), // Темно-серый
    onSecondary = Color.Black,
    background = Color.White,
    onBackground = Color.Black,
    surface = Color.White,
    onSurface = Color.Black,
    error = Color(0xFFB00020),
    onError = Color.White
)

@Composable
fun VkTestAppVideoPlayerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        content = content
    )
}