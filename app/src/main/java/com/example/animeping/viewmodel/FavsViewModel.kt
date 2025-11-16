package com.example.animeping.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.animeping.data.AnimeRepository
import com.example.animeping.data.UserRepository
import com.example.animeping.model.Anime
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class FavoritesUiState(
    val favoriteAnimes: List<Anime> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

class FavsViewModel : ViewModel() {

    private val animeRepository = AnimeRepository()

    private val _uiState = MutableStateFlow(FavoritesUiState())
    val uiState: StateFlow<FavoritesUiState> = _uiState.asStateFlow()

    fun loadFavoriteAnimes(context: Context) {
        val userRepository = UserRepository.getInstance(context)
        val favoriteIds = userRepository.getFavorites()

        _uiState.value = _uiState.value.copy(isLoading = true, error = null)

        viewModelScope.launch {
            try {
                if (favoriteIds.isEmpty()) {
                    _uiState.value = _uiState.value.copy(isLoading = false, favoriteAnimes = emptyList())
                    return@launch
                }

                val animes = favoriteIds.mapNotNull { id ->
                    try {
                        animeRepository.getAnimeById(id)
                    } catch (e: Exception) {
                        null
                    }
                }

                _uiState.value = _uiState.value.copy(isLoading = false, favoriteAnimes = animes)

            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(isLoading = false, error = "Failed to load favorites.")
            }
        }
    }
}