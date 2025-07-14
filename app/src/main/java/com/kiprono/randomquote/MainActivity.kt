package com.kiprono.randomquote

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue // Make sure this is imported
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.lifecycleScope // <-- ADDED THIS IMPORT
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.kiprono.randomquote.data.AppDatabase
import com.kiprono.randomquote.data.UserPreferencesRepository
import com.kiprono.randomquote.ui.ProfileScreen
import com.kiprono.randomquote.ui.theme.QuoteScreen
import com.kiprono.randomquote.ui.theme.RandomQuoteTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val userPreferencesRepository = remember { UserPreferencesRepository(applicationContext) }
            val isDarkTheme by userPreferencesRepository.isDarkTheme.collectAsState(initial = isSystemInDarkTheme())

            RandomQuoteTheme(darkTheme = isDarkTheme) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val context = LocalContext.current

                    val database = AppDatabase.getDatabase(context)
                    val favoriteQuoteDao = database.favoriteQuoteDao()

                    val quoteViewModel: QuoteViewModel = viewModel(
                        factory = QuoteViewModelFactory(
                            favoriteQuoteDao = favoriteQuoteDao,
                            userPreferencesRepository = userPreferencesRepository
                        )
                    )

                    QuoteApp(
                        viewModel = quoteViewModel,
                        isDarkTheme = isDarkTheme,
                        toggleTheme = {
                            // Ensure this cast is safe and the scope is correct
                            (context as? ComponentActivity)?.lifecycleScope?.launch {
                                userPreferencesRepository.saveThemePreference(!isDarkTheme)
                            }
                        }
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
            QuoteScreen(
                quote = currentQuote,
                isLoading = isLoading,
                errorMessage = errorMessage,
                onRefreshQuote = { viewModel.getRandomQuote() },
                onFavoriteQuote = { viewModel.addCurrentQuoteToFavorites() },
                onShareQuote = { viewModel.incrementSharedQuotes() },
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
                viewModel = viewModel,
                isDarkTheme = isDarkTheme,
                toggleTheme = toggleTheme
            )
        }
    }
}