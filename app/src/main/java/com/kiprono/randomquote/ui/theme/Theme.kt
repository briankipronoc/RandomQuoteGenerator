package com.kiprono.randomquote.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp // Import sp for font sizes
import com.kiprono.randomquote.R // Make sure R is imported

// Define your custom font family for Poppins
val PoppinsFontFamily = FontFamily(
    Font(R.font.poppins_regular, FontWeight.Normal),
    Font(R.font.poppins_bold, FontWeight.Bold),
    // Add other weights like Light, Medium, SemiBold if you downloaded them
    // Example: Font(R.font.poppins_light, FontWeight.Light),
    // Example: Font(R.font.poppins_medium, FontWeight.Medium),
    // Example: Font(R.font.poppins_semibold, FontWeight.SemiBold),
)

// Define your custom typography using Poppins
// FIX: Initialize Typography directly without MaterialTheme.typography.copy()
val AppTypography = androidx.compose.material3.Typography(
    displayLarge = androidx.compose.material3.Typography().displayLarge.copy(fontFamily = PoppinsFontFamily, fontWeight = FontWeight.Normal),
    displayMedium = androidx.compose.material3.Typography().displayMedium.copy(fontFamily = PoppinsFontFamily, fontWeight = FontWeight.Normal),
    displaySmall = androidx.compose.material3.Typography().displaySmall.copy(fontFamily = PoppinsFontFamily, fontWeight = FontWeight.Normal),

    headlineLarge = androidx.compose.material3.Typography().headlineLarge.copy(fontFamily = PoppinsFontFamily, fontWeight = FontWeight.Bold),
    headlineMedium = androidx.compose.material3.Typography().headlineMedium.copy(fontFamily = PoppinsFontFamily, fontWeight = FontWeight.SemiBold),
    headlineSmall = androidx.compose.material3.Typography().headlineSmall.copy(fontFamily = PoppinsFontFamily, fontWeight = FontWeight.SemiBold),

    titleLarge = androidx.compose.material3.Typography().titleLarge.copy(fontFamily = PoppinsFontFamily, fontWeight = FontWeight.Bold),
    titleMedium = androidx.compose.material3.Typography().titleMedium.copy(fontFamily = PoppinsFontFamily, fontWeight = FontWeight.SemiBold),
    titleSmall = androidx.compose.material3.Typography().titleSmall.copy(fontFamily = PoppinsFontFamily, fontWeight = FontWeight.Medium),

    bodyLarge = androidx.compose.material3.Typography().bodyLarge.copy(fontFamily = PoppinsFontFamily, fontWeight = FontWeight.Normal),
    bodyMedium = androidx.compose.material3.Typography().bodyMedium.copy(fontFamily = PoppinsFontFamily, fontWeight = FontWeight.Normal),
    bodySmall = androidx.compose.material3.Typography().bodySmall.copy(fontFamily = PoppinsFontFamily, fontWeight = FontWeight.Normal),

    labelLarge = androidx.compose.material3.Typography().labelLarge.copy(fontFamily = PoppinsFontFamily),
    labelMedium = androidx.compose.material3.Typography().labelMedium.copy(fontFamily = PoppinsFontFamily),
    labelSmall = androidx.compose.material3.Typography().labelSmall.copy(fontFamily = PoppinsFontFamily),
)

// Define a more vibrant color scheme, keeping text black/white on surfaces
private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFBB86FC), // A vibrant primary purple
    onPrimary = Color.Black,
    primaryContainer = Color(0xFF985EEF), // Slightly darker primary for containers
    onPrimaryContainer = Color.White,
    secondary = Color(0xFF03DAC5), // A vibrant secondary teal
    onSecondary = Color.Black,
    tertiary = Color(0xFF3700B3), // A complementary tertiary color
    onTertiary = Color.White,

    background = Color(0xFF1A1A1A), // Darker background
    onBackground = Color(0xFFFFFFFF), // White text on dark background

    surface = Color(0xFF2C2C2C), // Darker surface for cards, etc.
    onSurface = Color(0xFFFFFFFF), // White text on dark surface

    surfaceVariant = Color(0xFF424242), // Darker variant for subtle distinction
    onSurfaceVariant = Color(0xFFE0E0E0), // Light gray text for variant surfaces

    surfaceContainerHigh = Color(0xFF3A3A3A), // For "glass" effect in dark theme, slightly more opaque
    error = Color(0xFFCF6679), // Error red
    onError = Color.Black
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF673AB7), // A vibrant primary purple
    onPrimary = Color.White,
    primaryContainer = Color(0xFF8C5AE5), // Slightly lighter primary for containers
    onPrimaryContainer = Color.White,
    secondary = Color(0xFF00BFA5), // A vibrant secondary teal
    onSecondary = Color.White,
    tertiary = Color(0xFF673AB7), // A complementary tertiary color
    onTertiary = Color.White,

    background = Color(0xFFF0F2F5), // Light gray background
    onBackground = Color(0xFF1C1B1F), // Dark text on light background

    surface = Color(0xFFFFFFFF), // White surface for cards, etc.
    onSurface = Color(0xFF1C1B1F), // Dark text on white surface

    surfaceVariant = Color(0xFFEFEFEF), // Slightly darker variant for subtle distinction
    onSurfaceVariant = Color(0xFF49454F), // Darker gray text for variant surfaces

    surfaceContainerHigh = Color(0xFFF7F7F7), // For "glass" effect in light theme, very light
    error = Color(0xFFB00020), // Error red
    onError = Color.White
)

@Composable
fun RandomQuoteTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography, // Use your custom typography
        content = content
    )
}
