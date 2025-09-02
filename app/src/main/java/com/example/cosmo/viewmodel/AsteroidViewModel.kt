package com.example.cosmo.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cosmo.model.Asteroid
import com.example.cosmo.model.AsteroidRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AsteroidViewModel @Inject constructor(
    private val repository: AsteroidRepository
): ViewModel() {

    // Holds list of asteroids
    private val _asteroids = MutableStateFlow<List<Asteroid>>(emptyList())
    val asteroids: StateFlow<List<Asteroid>> = _asteroids

    // Loading state
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    // Error state
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    // Function to fetch asteroids for given date
    fun fetchAsteroids(date: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            try {
                val result = repository.fetchAsteroids(date)
                _asteroids.value = result
                result.forEach { asteroid ->
                    println("Asteroid: ${asteroid.name}, diameter: ${asteroid.diameter}")
                }
            } catch (e: Exception){
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }
}