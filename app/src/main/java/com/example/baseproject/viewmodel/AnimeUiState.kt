package com.example.baseproject.viewmodel

import com.example.baseproject.model.Anime

data class animeListUiState(
    val animes: List<Anime> = emptyList(),
    val isLoading: Boolean = false,
    val error: String ? = null,
    val limit: Int = 20,
    val offset: Int = 0,
    )

data class animeDetailState(
    val animes: Anime? = null,
    val isLoading: Boolean = false,
    val error: String ? = null,
)

