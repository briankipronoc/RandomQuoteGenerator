package com.example.randomquote

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.randomquote.ui.theme.RandomQuoteTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RandomQuoteTheme {
                QuoteApp()
            }
        }
    }
}

@Composable
fun QuoteApp() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "random_quote") {
        composable("random_quote") {
            RandomQuoteScreen(navController = navController)
        }
        composable("favorites") {
            FavoritesScreen(navController = navController)
        }
    }
}