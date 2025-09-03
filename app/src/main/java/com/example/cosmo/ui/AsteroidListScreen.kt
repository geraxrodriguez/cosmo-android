package com.example.cosmo.ui

import androidx.compose.foundation.layout.Column // Vertical layout container
import androidx.compose.foundation.layout.fillMaxWidth // Fills available horizontal space
import androidx.compose.foundation.layout.padding // adds spacing around a Composable
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator // Spinning loading indicator
import androidx.compose.material3.MaterialTheme // Provides some colors, typography, etc
import androidx.compose.material3.Text // Displays text in UI
import androidx.compose.runtime.* // Provides Compose runtime functions, like @Composable
import androidx.compose.ui.Modifier // Used to modify Composable
import androidx.compose.ui.unit.dp // Unit for sizing and spacing in Compose
import androidx.hilt.navigation.compose.hiltViewModel // Hilt-powered ViewModel injection
import com.example.cosmo.viewmodel.AsteroidViewModel //

@Composable
fun AsteroidListScreen(
    modifier: Modifier = Modifier, // So callers can pass styling
    viewModel: AsteroidViewModel = hiltViewModel() // Inject ViewModel w/ Hilt
) {
    // Convert StateFlows from ViewModel into Compose State
    val asteroids by viewModel.asteroids.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    // Fetch data once when screen first appears
    LaunchedEffect(Unit) {
        viewModel.fetchAsteroids("2024-01-01") // temporary hardcoded date
    }

    Column(
        modifier = Modifier
            .padding(10.dp)
            .verticalScroll(rememberScrollState()) // enables scrolling
    ) {
        when {
            isLoading -> {
                CircularProgressIndicator()
            }
            error != null -> {
                Text(
                    text = "Error: $error",
                    color = MaterialTheme.colorScheme.error
                )
            }
            asteroids.isNotEmpty() -> {
                asteroids.forEach { asteroid ->
                    Text(
                        text = "☄\uFE0F ${asteroid.name} - ${asteroid.diameter} ft",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    )
                }
            }
            else -> {
                Text("No asteroids found.")
            }
        }
    }
}
