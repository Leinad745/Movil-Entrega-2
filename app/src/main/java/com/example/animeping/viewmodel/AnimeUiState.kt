package com.example.animeping.viewmodel

import com.example.animeping.model.Anime

data class animeListUiState(
    val animes: List<Anime> = emptyList(),
    val isLoading: Boolean = false,
    val error: String ? = null,
    val limit: Int = 20,
    val offset: Int = 0,
    val hasMoreData: Boolean = true
    )

data class animeDetailState(
    val anime: Anime? = null,
    val isLoading: Boolean = false,
    val error: String ? = null,
)

