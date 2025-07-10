package com.kiprono.randomquote

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.compose.runtime.LaunchedEffect
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FormatQuote
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.kiprono.randomquote.data.Quote
import com.kiprono.randomquote.ui.theme.* // Import your theme components

// Define navigation routes
object Destinations {
    const val QUOTE_SCREEN = "quote_screen"
    const val FAVORITES_SCREEN = "favorites_screen"
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Updated to use RandomQuoteTheme (assuming this is the correct theme name in your Theme.kt)
            RandomQuoteTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // Start navigation from here
                    AppNavigation()
                }
            }
        }
    }
}

// Added AppNavigation Composable to manage screens
@Composable
fun AppNavigation(quoteViewModel: QuoteViewModel = viewModel()) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Destinations.QUOTE_SCREEN
    ) {
        // Route for the main Quote Screen
        composable(Destinations.QUOTE_SCREEN) {
            QuoteScreen(
                quoteViewModel = quoteViewModel,
                navController = navController // Pass NavController to enable navigation
            )
        }

        // Route for the Favorites Screen
        composable(Destinations.FAVORITES_SCREEN) {
            // Note: This requires FavoritesScreen.kt to be in place
            FavoritesScreen(
                navController = navController,
                viewModel = quoteViewModel
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
// Updated QuoteScreen to accept NavController
fun QuoteScreen(
    quoteViewModel: QuoteViewModel = viewModel(),
    navController: NavController
) {
    // ... (existing state variables)
    val currentQuote by quoteViewModel.quoteState.collectAsState()
    val isLoading by quoteViewModel.isLoading.collectAsState()
    val errorMessage by quoteViewModel.errorMessage.collectAsState()
    val context = LocalContext.current

    // ... (LaunchedEffect)
    LaunchedEffect(Unit) {
        quoteViewModel.getRandomQuote()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Random Quote Generator") },
                // Added action to navigate to FavoritesScreen
                actions = {
                    IconButton(onClick = { navController.navigate(Destinations.FAVORITES_SCREEN) }) {
                        Icon(imageVector = Icons.Default.Favorite, contentDescription = "Favorites")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(DarkGreyPrimary)
                .padding(padding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Quote Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = DarkGreySurface),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.FormatQuote,
                        contentDescription = "Quote Icon",
                        tint = MutedText,
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    AnimatedContent(
                        targetState = currentQuote,
                        transitionSpec = {
                            fadeIn(animationSpec = tween(durationMillis = 300, delayMillis = 150)) togetherWith
                                    fadeOut(animationSpec = tween(durationMillis = 150))
                        }, label = "quote_animation"
                    ) { targetQuote ->
                        if (targetQuote != null) {
                            Text(
                                text = targetQuote.content,
                                fontFamily = FontFamily.Serif,
                                fontWeight = FontWeight.Normal,
                                fontSize = 24.sp,
                                lineHeight = 32.sp,
                                textAlign = TextAlign.Center,
                                color = LightText
                            )
                        } else {
                            Text(
                                text = "Tap 'New Quote' to get started!",
                                fontFamily = FontFamily.Serif,
                                fontWeight = FontWeight.Normal,
                                fontSize = 24.sp,
                                lineHeight = 32.sp,
                                textAlign = TextAlign.Center,
                                color = MutedText
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    currentQuote?.author?.let { author ->
                        Text(
                            text = "- $author",
                            fontFamily = FontFamily.SansSerif,
                            fontWeight = FontWeight.Light,
                            fontSize = 18.sp,
                            textAlign = TextAlign.End,
                            modifier = Modifier.fillMaxWidth(),
                            color = MutedText
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Loading Indicator and Buttons
            if (isLoading) {
                CircularProgressIndicator(color = AccentGreen)
            } else {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Share Button
                    Button(
                        onClick = {
                            currentQuote?.let { quote ->
                                shareQuote(context, quote.content, quote.author)
                            }
                        },
                        enabled = currentQuote != null,
                        colors = ButtonDefaults.buttonColors(containerColor = AccentGreen),
                        shape = RoundedCornerShape(50)
                    ) {
                        Icon(imageVector = Icons.Default.Share, contentDescription = "Share Quote")
                        Spacer(Modifier.width(8.dp))
                        Text("Share")
                    }

                    // New Quote Button
                    Button(
                        onClick = { quoteViewModel.getRandomQuote() },
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                        shape = RoundedCornerShape(50)
                    ) {
                        Text("New Quote")
                    }
                }
            }

            // Error Message
            errorMessage?.let { message ->
                Spacer(modifier = Modifier.height(24.dp))
                Text(
                    text = message,
                    color = ErrorRed,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(ErrorRed.copy(alpha = 0.1f), RoundedCornerShape(8.dp))
                        .padding(12.dp)
                )
            }
        }
    }
}

// ... (shareQuote function remains the same)
fun shareQuote(context: Context, quoteContent: String, quoteAuthor: String) {
    val shareIntent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_TEXT, "\"$quoteContent\" - $quoteAuthor")
        type = "text/plain"
    }
    context.startActivity(Intent.createChooser(shareIntent, "Share quote via"))
}

// Updated Preview to reflect navigation (since it needs a NavController for the preview)
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    RandomQuoteTheme {
        // Provide a dummy NavController for the preview
        QuoteScreen(navController = rememberNavController())
    }
}