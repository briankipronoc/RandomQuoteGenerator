// In your com.kiprono.randomquote.ui.theme package, create Typography.kt
package com.kiprono.randomquote.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
// import your custom fonts here if you've added them
// import com.kiprono.randomquote.R // Example for accessing fonts in resources

// Define your custom font families if you have them, e.g.:
// val YourCustomFont = FontFamily(
//     Font(R.font.your_font_regular, FontWeight.Normal),
//     Font(R.font.your_font_bold, FontWeight.Bold)
// )

val Typography = Typography(
    headlineMedium = TextStyle(
        fontFamily = FontFamily.Default, // Or YourCustomFont if defined
        fontWeight = FontWeight.Normal,
        fontSize = 28.sp,
        lineHeight = 36.sp,
        letterSpacing = 0.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default, // Or YourCustomFont
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = FontFamily.Default, // Or YourCustomFont
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 20.sp,
        letterSpacing = 0.25.sp
    ),
    bodySmall = TextStyle(
        fontFamily = FontFamily.Default, // Or YourCustomFont
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.4.sp
    )
    /* Other typography styles...
    titleLarge = TextStyle(...)
    labelSmall = TextStyle(...)
    */
)