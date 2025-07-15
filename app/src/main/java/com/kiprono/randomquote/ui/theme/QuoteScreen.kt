package com.kiprono.randomquote.ui.theme

import android.content.Intent
import android.util.Log
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.input.pointer.pointerInput
import androidx.navigation.NavController // Import NavController
import com.kiprono.randomquote.data.Quote
import com.kiprono.randomquote.ui.viewmodel.QuoteViewModel // Import QuoteViewModel
import kotlinx.coroutines.launch
import java.util.Calendar

// Define the TAG for logging in this file
private const val TAG = "QuoteScreen"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuoteScreen(
    viewModel: QuoteViewModel, // FIX: Added ViewModel parameter
    navController: NavController, // FIX: Added NavController parameter
    quote: Quote?,
    isLoading: Boolean,
    errorMessage: String?,
    onRefreshQuote: () -> Unit,
    onFavoriteQuote: () -> Unit,
    onShareQuote: () -> Unit,
    onNavigateToProfile: () -> Unit,
    userName: String?,
    isDarkTheme: Boolean,
    toggleTheme: () -> Unit,
    isQuoteFavorited: Boolean
) {
    val haptic = LocalHapticFeedback.current
    val context = LocalContext.current
    val interactionSource = remember { MutableInteractionSource() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Daily Dose",
                        style = AppTypography.headlineLarge.copy(fontWeight = FontWeight.ExtraBold),
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.primary
                    )
                },
                actions = {
                    IconButton(onClick = toggleTheme) {
                        Icon(
                            imageVector = if (isDarkTheme) Icons.Default.LightMode else Icons.Default.DarkMode,
                            contentDescription = "Toggle theme",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    IconButton(onClick = onNavigateToProfile) {
                        Icon(
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = "Profile",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(top = 16.dp, bottom = 24.dp)
            ) {
                Text(
                    text = getGreeting(userName),
                    style = AppTypography.headlineMedium.copy(fontWeight = FontWeight.Normal),
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
                Spacer(Modifier.height(8.dp))
                if (userName.isNullOrEmpty()) {
                    Text(
                        text = "Hint: Enter your name in the Profile screen to personalize your experience!",
                        style = AppTypography.bodySmall.copy(fontStyle = FontStyle.Italic),
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                    )
                }
            }

            Spacer(Modifier.weight(1f))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(horizontal = 8.dp)
                    .clickable(
                        interactionSource = interactionSource,
                        indication = rememberRipple(),
                        onClick = {
                            Log.d(TAG, "Single-tapped the quote card (for ripple).")
                        }
                    )
                    .pointerInput(Unit) {
                        detectTapGestures(
                            onTap = {
                                Log.d(TAG, "Single-tapped the quote card.")
                            },
                            onDoubleTap = {
                                onFavoriteQuote()
                                Log.d(TAG, "Double-tapped to favorite/unfavorite")
                            },
                            onLongPress = {
                                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                                Log.d(TAG, "Long-pressed for haptic feedback")
                            }
                        )
                    },
                elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainerHigh.copy(alpha = 0.9f)
                ),
                shape = MaterialTheme.shapes.extraLarge
            ) {
                Column(
                    modifier = Modifier.padding(28.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(64.dp),
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            "Loading wisdom...",
                            modifier = Modifier.padding(top = 16.dp),
                            style = AppTypography.headlineSmall,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    } else if (errorMessage != null) {
                        Text(
                            text = "Error: $errorMessage \n\nPlease try again later.",
                            color = MaterialTheme.colorScheme.error,
                            textAlign = TextAlign.Center,
                            style = AppTypography.bodyLarge
                        )
                    } else if (quote != null) {
                        Text(
                            text = "\"${quote.text ?: "Quote content unavailable"}\"", // FIX: Changed quote.content to quote.text
                            style = AppTypography.headlineSmall.copy(
                                fontWeight = FontWeight.Bold,
                                lineHeight = 36.sp
                            ),
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.padding(bottom = 24.dp)
                        )
                        Text(
                            text = "- ${quote.author ?: "Unknown"}",
                            style = AppTypography.bodyLarge.copy(fontStyle = FontStyle.Italic),
                            textAlign = TextAlign.End,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.fillMaxWidth()
                        )
                    } else {
                        Text(
                            text = "Tap the refresh button to discover your first dose of daily wisdom!",
                            style = AppTypography.headlineSmall,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                        )
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = {
                    onFavoriteQuote()
                    Log.d(TAG, "Favorite button clicked.")
                }) {
                    Icon(
                        imageVector = if (isQuoteFavorited) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                        contentDescription = "Favorite Quote",
                        tint = if (isQuoteFavorited) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(36.dp)
                    )
                }

                Spacer(Modifier.width(32.dp))

                IconButton(
                    onClick = {
                        quote?.let { currentQuote ->
                            val shareText = "\"${currentQuote.text}\" - ${currentQuote.author}" // FIX: Changed currentQuote.content to currentQuote.text
                            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                type = "text/plain"
                                putExtra(Intent.EXTRA_TEXT, shareText)
                                putExtra(Intent.EXTRA_SUBJECT, "A great quote from RandomQuote!")
                            }
                            context.startActivity(Intent.createChooser(shareIntent, "Share this quote via"))
                            onShareQuote()
                            Log.d(TAG, "Share button clicked.")
                        } ?: Log.w(TAG, "Share button clicked but no quote available.")
                    }
                ) {
                    Icon(
                        imageVector = Icons.Filled.Share,
                        contentDescription = "Share Quote",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(36.dp)
                    )
                }
            }

            Spacer(Modifier.weight(1f))

            FloatingActionButton(
                onClick = onRefreshQuote,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier
                    .size(68.dp)
                    .align(Alignment.CenterHorizontally)
            ) {
                Icon(
                    imageVector = Icons.Filled.Refresh,
                    contentDescription = "Refresh Quote",
                    modifier = Modifier.size(36.dp)
                )
            }
            Spacer(Modifier.height(16.dp))
        }
    }
}

@Composable
fun AnimatedInteractionIcon(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    contentDescription: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    tint: androidx.compose.ui.graphics.Color = LocalContentColor.current
) {
    val scale = remember { Animatable(1f) }
    val coroutineScope = rememberCoroutineScope()

    IconButton(
        onClick = {
            onClick()
            coroutineScope.launch {
                scale.animateTo(0.8f, animationSpec = tween(durationMillis = 100))
                scale.animateTo(1f, animationSpec = spring(dampingRatio = 0.5f, stiffness = 200f))
            }
        },
        modifier = modifier.scale(scale.value)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            tint = tint,
            modifier = Modifier.size(36.dp)
        )
    }
}

@Composable
fun getGreeting(userName: String?): String {
    val calendar = Calendar.getInstance()
    val hour = calendar.get(Calendar.HOUR_OF_DAY)

    val (greetingText, emoji) = when (hour) {
        in 0..11 -> "Good Morning" to "☀️"
        in 12..17 -> "Good Afternoon" to "🌇"
        else -> "Good Evening" to "🌙"
    }

    return if (!userName.isNullOrEmpty()) {
        "$greetingText, $userName! $emoji"
    } else {
        "$greetingText! Welcome to your daily dose of wisdom. 👋"
    }
}