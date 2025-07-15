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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.lifecycleScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.kiprono.randomquote.data.UserPreferencesRepository
import com.kiprono.randomquote.ui.FavoriteQuotesScreen
import com.kiprono.randomquote.ui.ProfileScreen
import com.kiprono.randomquote.ui.theme.QuoteScreen
import com.kiprono.randomquote.ui.theme.RandomQuoteTheme
import com.kiprono.randomquote.ui.viewmodel.QuoteViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var userPreferencesRepository: UserPreferencesRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val isDarkTheme by userPreferencesRepository.isDarkTheme.collectAsState(initial = isSystemInDarkTheme())

            RandomQuoteTheme(darkTheme = isDarkTheme) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    QuoteApp(
                        isDarkTheme = isDarkTheme,
                        toggleTheme = {
                            lifecycleScope.launch {
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
    isDarkTheme: Boolean,
    toggleTheme: () -> Unit
) {
    val navController = rememberNavController()
    val viewModel: QuoteViewModel = hiltViewModel()

    val userName by viewModel.userName.collectAsState()
    val currentQuote by viewModel.quoteState.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    // FIX: Provide an initial value for filteredFavorites
    val allFavorites by viewModel.filteredFavorites.collectAsState(initial = emptyList())

    val isQuoteFavorited = remember(currentQuote, allFavorites) {
        currentQuote?.let { quoteNonNull ->
            allFavorites.any { favoriteQuote ->
                favoriteQuote.text == quoteNonNull.text && favoriteQuote.author == quoteNonNull.author
            }
        } ?: false
    }

    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            QuoteScreen(
                viewModel = viewModel,
                navController = navController,
                quote = currentQuote,
                isLoading = isLoading,
                errorMessage = errorMessage,
                onRefreshQuote = { viewModel.getRandomQuote() },
                onFavoriteQuote = { viewModel.addOrRemoveFavorite() },
                onShareQuote = { viewModel.incrementSharedQuotes() },
                onNavigateToProfile = { navController.navigate("profile") },
                userName = userName,
                isDarkTheme = isDarkTheme,
                toggleTheme = toggleTheme,
                isQuoteFavorited = isQuoteFavorited
            )
        }

        composable("favorite_quotes_screen") {
            FavoriteQuotesScreen(
                navController = navController,
                viewModel = viewModel
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