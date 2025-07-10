package com.kiprono.randomquote.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color // <--- ADD THIS IMPORT
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// Define a dark color scheme using your custom colors
private val DarkColorScheme = darkColorScheme(
    primary = AccentGreen, // Main accent color
    secondary = MutedText,
    tertiary = MutedText,
    background = DarkGreyPrimary, // Main background
    surface = DarkGreySurface, // Card background
    onPrimary = Color.Black, // Text on primary color
    onSecondary = LightText,
    onTertiary = LightText,
    onBackground = LightText, // Text on background
    onSurface = LightText, // Text on surface
    error = ErrorRed,
    onError = Color.Black
)

// Define a light color scheme (optional, but good for completeness)
private val LightColorScheme = lightColorScheme(
    primary = AccentGreen,
    secondary = Color(0xFF6200EE), // Default Material color
    tertiary = Color(0xFF03DAC5), // Default Material color
    background = Color(0xFFFFFFFF),
    surface = Color(0xFFFFFFFF),
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onTertiary = Color.Black,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    error = ErrorRed,
    onError = Color.White
)

@Composable
fun RandomQuoteGeneratorTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false, // Set to true if you want dynamic colors on Android 12+
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
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb() // Status bar matches background
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme // Adjust status bar icons
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography, // Assuming Typography is defined in Typography.kt
        content = content
    )
}