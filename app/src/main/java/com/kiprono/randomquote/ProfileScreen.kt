package com.kiprono.randomquote.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons // Keep this for the 'Icons' object itself
import androidx.compose.material.icons.filled.* // Import ALL filled icons with a wildcard
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController

import com.kiprono.randomquote.data.Achievement // Import the Achievement data class
import com.kiprono.randomquote.ui.viewmodel.QuoteViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavController,
    viewModel: QuoteViewModel = hiltViewModel(),
    isDarkTheme: Boolean,
    toggleTheme: () -> Unit
) {
    val userName by viewModel.userName.collectAsState()
    val quotesReadCount by viewModel.quotesReadCount.collectAsState()
    val favoriteQuotesCount by viewModel.favoriteQuotesCount.collectAsState()
    val quotesSharedCount by viewModel.quotesSharedCount.collectAsState()

    var nameInput by remember(userName) { mutableStateOf(userName) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Your Profile",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                },
                actions = {
                    IconButton(onClick = toggleTheme) {
                        Icon(
                            imageVector = if (isDarkTheme) Icons.Default.LightMode else Icons.Default.DarkMode,
                            contentDescription = "Toggle theme"
                        )
                    }
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Filled.Home,
                            contentDescription = "Home"
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
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(16.dp))
            Icon(
                imageVector = Icons.Filled.AccountCircle,
                contentDescription = "Profile Picture",
                modifier = Modifier.size(96.dp).clip(CircleShape),
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(Modifier.height(16.dp))
            Text(
                text = "Hello, $userName!",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(Modifier.height(8.dp))

            OutlinedTextField(
                value = nameInput,
                onValueChange = { nameInput = it },
                label = { Text("Your Name", style = MaterialTheme.typography.bodyMedium) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Words),
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = {
                    if (nameInput.isNotBlank() && nameInput != userName) {
                        IconButton(onClick = { viewModel.saveUserName(nameInput) }) {
                            Icon(Icons.Filled.Save, "Save Name", tint = MaterialTheme.colorScheme.primary)
                        }
                    }
                }
            )
            Spacer(Modifier.height(24.dp))

            Text(
                text = "Your Quote Journey",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.align(Alignment.Start)
            )
            Spacer(Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                StatItem(icon = Icons.Filled.Autorenew, label = "Read", count = quotesReadCount)
                StatItem(icon = Icons.Filled.Favorite, label = "Liked", count = favoriteQuotesCount)
                StatItem(icon = Icons.Filled.Share, label = "Shared", count = quotesSharedCount)
            }

            Spacer(Modifier.height(32.dp))

            Text(
                text = "Achievements",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            val achievements by viewModel.achievements.collectAsState()
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(max = 200.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (achievements.isEmpty()) {
                    item {
                        Text(
                            "No achievements yet. Keep going!",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                } else {
                    items(achievements) { achievement ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = if (achievement.unlocked) MaterialTheme.colorScheme.secondaryContainer else MaterialTheme.colorScheme.surfaceVariant
                            ),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    achievement.title,
                                    style = MaterialTheme.typography.titleMedium
                                )
                                Text(
                                    achievement.description,
                                    style = MaterialTheme.typography.bodySmall
                                )
                                if (achievement.unlocked) {
                                    Text(
                                        "Unlocked!",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                } else {
                                    Text(
                                        "Progress: ${viewModel.getAchievementProgress(achievement)}/${achievement.threshold}",
                                        style = MaterialTheme.typography.labelSmall
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // THIS IS THE NAVIGATION BUTTON TO FAVORITES SCREEN
            Button(
                onClick = { navController.navigate("favorite_quotes_screen") }, // Corrected route string
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Favorite, contentDescription = "Favorite Quotes")
                    Spacer(Modifier.width(8.dp))
                    Text("View Favorite Quotes ($favoriteQuotesCount)")
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
        Text(text = label, style = MaterialTheme.typography.bodyMedium)
        Text(text = count.toString(), style = MaterialTheme.typography.headlineSmall)
    }
}