package com.kiprono.randomquote.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
// Import AutoMirrored for ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.kiprono.randomquote.QuoteViewModel
import com.kiprono.randomquote.data.Quote
import com.kiprono.randomquote.ui.theme.AppTypography

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavController, viewModel: QuoteViewModel) {
    val userName by viewModel.userName.collectAsState()
    val quotesReadCount by viewModel.quotesReadCount.collectAsState()
    val quotesLikedCount by viewModel.quotesLikedCount.collectAsState()
    val quotesSharedCount by viewModel.quotesSharedCount.collectAsState()
    val allFavorites by viewModel.allFavorites.collectAsState()

    var nameInput by remember(userName) { mutableStateOf(userName ?: "") }
    var searchQuery by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Your Profile", style = AppTypography.titleLarge) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        // FIX: Use Icons.AutoMirrored.Filled.ArrowBack
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
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
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Profile Header
            Spacer(Modifier.height(16.dp))
            Icon(
                imageVector = Icons.Filled.AccountCircle,
                contentDescription = "Profile Picture",
                modifier = Modifier
                    .size(96.dp)
                    .clip(CircleShape),
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(Modifier.height(16.dp))
            Text(
                text = userName?.let { "Hello, $it!" } ?: "Hello there!",
                style = AppTypography.headlineLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(Modifier.height(8.dp))

            // Name Input Field
            OutlinedTextField(
                value = nameInput,
                onValueChange = { nameInput = it },
                label = { Text("Your Name", style = AppTypography.bodyMedium) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Words),
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = {
                    if (nameInput.isNotBlank() && nameInput != userName.orEmpty()) {
                        IconButton(onClick = { viewModel.saveUserName(nameInput) }) {
                            Icon(Icons.Filled.Save, "Save Name", tint = MaterialTheme.colorScheme.primary)
                        }
                    }
                }
            )
            Spacer(Modifier.height(24.dp))

            // Stats Section
            Text(
                text = "Your Quote Journey",
                style = AppTypography.headlineSmall,
                modifier = Modifier.align(Alignment.Start)
            )
            Spacer(Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                StatItem(icon = Icons.Filled.Autorenew, label = "Read", count = quotesReadCount)
                StatItem(icon = Icons.Filled.Favorite, label = "Liked", count = quotesLikedCount)
                StatItem(icon = Icons.Filled.Share, label = "Shared", count = quotesSharedCount)
            }

            Spacer(Modifier.height(32.dp))

            // Favorite Quotes Section
            Text(
                text = "Your Favorite Quotes",
                style = AppTypography.headlineSmall,
                modifier = Modifier.align(Alignment.Start)
            )
            Spacer(Modifier.height(16.dp))

            // Search Bar for Favorites
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Search Favorites", style = AppTypography.bodyMedium) },
                singleLine = true,
                leadingIcon = { Icon(Icons.Filled.Search, contentDescription = "Search") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(16.dp))

            val filteredFavorites = remember(allFavorites, searchQuery) {
                if (searchQuery.isBlank()) {
                    allFavorites
                } else {
                    allFavorites.filter {
                        // Safely call .contains() on potentially nullable content and author
                        it.content?.contains(searchQuery, ignoreCase = true) ?: false ||
                                it.author?.contains(searchQuery, ignoreCase = true) ?: false
                    }
                }
            }

            if (filteredFavorites.isEmpty()) {
                Text(
                    text = if (searchQuery.isBlank()) "No favorite quotes yet. Like some quotes on the main screen!" else "No matching favorites found.",
                    style = AppTypography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(16.dp),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
            } else {
                LazyColumn(modifier = Modifier.fillMaxWidth()) {
                    items(filteredFavorites) { quote ->
                        FavoriteQuoteItem(quote = quote) {
                            viewModel.removeFavorite(quote)
                        }
                        Spacer(Modifier.height(8.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun StatItem(icon: ImageVector, label: String, count: Int) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(imageVector = icon, contentDescription = label, modifier = Modifier.size(32.dp), tint = MaterialTheme.colorScheme.primary)
        Spacer(Modifier.height(4.dp))
        Text(text = label, style = AppTypography.bodyMedium)
        Text(text = count.toString(), style = AppTypography.headlineSmall)
    }
}

@Composable
fun FavoriteQuoteItem(quote: Quote, onRemove: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh.copy(alpha = 0.9f) // Applying glass-like effect
        ),
        shape = MaterialTheme.shapes.medium // Consistent shape
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "\"${quote.content ?: "No content"}\"", // Safely access content
                    style = AppTypography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "- ${quote.author ?: "Unknown"}", // Safely access author
                    style = AppTypography.bodySmall.copy(fontStyle = FontStyle.Italic),
                    modifier = Modifier.padding(top = 4.dp),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            IconButton(onClick = onRemove) {
                Icon(Icons.Filled.Delete, "Remove from favorites", tint = MaterialTheme.colorScheme.error)
            }
        }
    }
}