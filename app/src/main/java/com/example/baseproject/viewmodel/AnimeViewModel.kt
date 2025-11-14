package com.example.baseproject.viewmodel

import androidx.lifecycle.ViewModel
import com.example.baseproject.data.AnimeRepository
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow


class AnimeViewModel(
   private val repository: AnimeRepository = AnimeRepository()

): ViewModel() {

    private  val _listState = MutableStateFlow(animeListUiState())
    var listState: StateFlow<animeListUiState> = _listState.asStateFlow()
        private set

    private  val _detailState = MutableStateFlow(animeDetailState())
    var detailState: StateFlow<animeDetailState> = _detailState.asStateFlow()

    fun loadList(limit: Int = 20, offset: Int = 0) {
        _listState.value = _listState.value.copy(
            isLoading = true,
            error = null,
            limit = limit,
            offset = offset
        )
        viewModelScope.launch {
            try {
               val result = repository.getAnimeList(limit, offset)
                _listState.value = listState.value.copy(
                    animes = result.data,
                    isLoading = false,
                    error = null,
                )
            }catch (e: Exception) {
                e.printStackTrace()
                _listState.value.copy(
                    isLoading = false,
                    error = e.message
                )
            }
        }
    }

}