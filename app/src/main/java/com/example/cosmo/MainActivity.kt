package com.example.cosmo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.cosmo.ui.theme.CosmoTheme
import kotlinx.coroutines.*
import com.example.cosmo.model.AsteroidRepository
import android.util.Log
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var repository: AsteroidRepository // Hilt provides this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        testRepository()
        setContent {
            CosmoTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }

    private fun testRepository() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val asteroids = repository.fetchAsteroids("2024-01-01")
                println("Found ${asteroids.size} asteroids:")
                asteroids.forEach { asteroid ->
                    Log.d("_","- ${asteroid.name}: ${asteroid.diameter}ft, ${asteroid.velocity}mph, ${asteroid.missDistance}mi, Hazardous: ${asteroid.isHazardous}")
                }
            } catch (e: Exception) {
                println("Repository Error: ${e.message}")
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    CosmoTheme {
        Greeting("Android")
    }
}