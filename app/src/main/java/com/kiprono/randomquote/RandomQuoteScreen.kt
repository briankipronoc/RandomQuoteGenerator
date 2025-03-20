package com.kiprono.randomquote

import android.content.Intent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RandomQuoteScreen(navController: NavController, viewModel: QuoteViewModel = viewModel()) {
    val quote by viewModel.quoteState.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val context = LocalContext.current

    // Request a random quote when the screen appears
    LaunchedEffect(Unit) {
        viewModel.getRandomQuote()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Random Quote Generator") },
                actions = {
                    IconButton(onClick = { navController.navigate("favorites") }) {
                        Icon(imageVector = Icons.Default.Favorite, contentDescription = "Favorites")
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            if (isLoading) {
                CircularProgressIndicator()
            } else {
                quote?.let {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "\"${it.content}\"",
                            style = MaterialTheme.typography.headlineSmall,
                            modifier = Modifier.padding(8.dp)
                        )
                        Text(
                            text = "- ${it.author}",
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(8.dp)
                        )
                        Row(
                            modifier = Modifier.padding(top = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            IconButton(onClick = { viewModel.getRandomQuote() }) {
                                Icon(imageVector = Icons.Default.Refresh, contentDescription = "New Quote")
                            }
                            IconButton(onClick = {
                                // Share the quote using an intent
                                val shareIntent = Intent().apply {
                                    action = Intent.ACTION_SEND
                                    putExtra(Intent.EXTRA_TEXT, "\"${it.content}\" - ${it.author}")
                                    type = "text/plain"
                                }
                                context.startActivity(Intent.createChooser(shareIntent, "Share Quote"))
                            }) {
                                Icon(imageVector = Icons.Default.Share, contentDescription = "Share Quote")
                            }
                            IconButton(onClick = { viewModel.addCurrentQuoteToFavorites() }) {
                                Icon(imageVector = Icons.Default.Favorite, contentDescription = "Add to Favorites")
                            }
                        }
                    }
                } ?: Text("No quote available.")
            }
        }
    }
}