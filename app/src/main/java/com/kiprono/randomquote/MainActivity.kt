package com.kiprono.randomquote

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme // Keep this for initialDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.kiprono.randomquote.data.AppDatabase
import com.kiprono.randomquote.data.UserPreferencesRepository
import com.kiprono.randomquote.ui.ProfileScreen
import com.kiprono.randomquote.ui.theme.QuoteScreen // ADDED: Import your QuoteScreen
import com.kiprono.randomquote.ui.theme.RandomQuoteTheme

// REMOVED: Unused imports related to the removed QuoteContent composable
// import androidx.compose.foundation.ExperimentalFoundationApi
// import androidx.compose.foundation.combinedClickable
// import androidx.compose.foundation.layout.Arrangement
// import androidx.compose.foundation.layout.Column
// import androidx.compose.foundation.layout.Spacer
// import androidx.compose.foundation.layout.height
// import androidx.compose.foundation.layout.padding
// import androidx.compose.foundation.layout.Row
// import androidx.compose.foundation.layout.Box
// import androidx.compose.material.icons.Icons
// import androidx.compose.material.icons.filled.Info
// import androidx.compose.material.icons.filled.Refresh
// import androidx.compose.material.icons.filled.Share
// import androidx.compose.material3.Card
// import androidx.compose.material3.CircularProgressIndicator
// import androidx.compose.material3.Icon
// import androidx.compose.material3.IconButton
// import androidx.compose.ui.graphics.Color
// import androidx.compose.ui.platform.LocalHapticFeedback
// import androidx.compose.ui.hapticfeedback.HapticFeedbackType
// import androidx.compose.ui.text.font.FontWeight
// import androidx.compose.ui.unit.dp
// import java.util.Calendar

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val initialDarkTheme = isSystemInDarkTheme()
            var darkTheme by remember { mutableStateOf(initialDarkTheme) }

            RandomQuoteTheme(darkTheme = darkTheme) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val context = LocalContext.current

                    val database = AppDatabase.getDatabase(context)
                    val favoriteQuoteDao = database.favoriteQuoteDao()
                    val userPreferencesRepository = UserPreferencesRepository(context)

                    val quoteViewModel: QuoteViewModel = viewModel(
                        factory = QuoteViewModelFactory(
                            favoriteQuoteDao = favoriteQuoteDao,
                            userPreferencesRepository = userPreferencesRepository
                        )
                    )

                    QuoteApp(
                        viewModel = quoteViewModel,
                        isDarkTheme = darkTheme,
                        toggleTheme = { darkTheme = !darkTheme }
                    )
                }
            }
        }
    }
}

@Composable
fun QuoteApp(
    viewModel: QuoteViewModel,
    isDarkTheme: Boolean,
    toggleTheme: () -> Unit
) {
    val navController = rememberNavController()

    val userName by viewModel.userName.collectAsState()
    val currentQuote by viewModel.quoteState.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val allFavorites by viewModel.allFavorites.collectAsState()

    val isQuoteFavorited = remember(currentQuote, allFavorites) {
        currentQuote != null && allFavorites.any { it.content == currentQuote?.content && it.author == currentQuote?.author }
    }

    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            // Replaced QuoteContent with QuoteScreen
            QuoteScreen(
                quote = currentQuote,
                isLoading = isLoading,
                errorMessage = errorMessage,
                onRefreshQuote = { viewModel.getRandomQuote() },
                onFavoriteQuote = { viewModel.addCurrentQuoteToFavorites() },
                onShareQuote = { viewModel.incrementSharedQuotes() }, // ViewModel still just increments a count
                onNavigateToProfile = { navController.navigate("profile") },
                userName = userName,
                isDarkTheme = isDarkTheme,
                toggleTheme = toggleTheme,
                isQuoteFavorited = isQuoteFavorited
            )
        }

        composable("profile") {
            ProfileScreen(
                navController = navController,
                viewModel = viewModel
            )
        }
    }
}

// REMOVED THE ENTIRE QuoteContent COMPOSABLE FROM HERE
/*
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun QuoteContent(
    quote: com.kiprono.randomquote.data.Quote?,
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
    // ... (rest of QuoteContent logic, now moved to QuoteScreen)
}
*/