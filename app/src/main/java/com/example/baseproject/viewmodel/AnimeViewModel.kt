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

    fun loadList(limit: Int = 20, offset: Int = 0, loadMore: Boolean = false) {
        if(!loadMore) {
            _listState.value = _listState.value.copy(
                isLoading = true,
                error = null
            )
        }

        viewModelScope.launch {
            try {
               val result = repository.getAnimeList(limit, offset)
                val newAnimes = result.data ?: emptyList()
                _listState.value = listState.value.copy(
                    animes = if (loadMore) _listState.value.animes + newAnimes else newAnimes,
                    isLoading = false,
                    error = null,
                    limit = limit,
                    offset = offset + newAnimes.size,
                    hasMoreData =  newAnimes.size == limit

                )
            }catch (e: Exception) {
                e.printStackTrace()
                _listState.value =_listState.value.copy(
                    isLoading = false,
                    error = e.message
                )
            }
        }
    }

    fun loadAnimeDetail(animeId: String) {
        _detailState.value = _detailState.value.copy(
            isLoading = true,
            error = null ,
            anime = null
        )
        viewModelScope.launch {
            try {
                val animeDetail = repository.getAnimeById(animeId)
                if(animeDetail != null) {
                    _detailState.value = _detailState.value.copy(
                        anime = animeDetail ,
                        isLoading = false,
                        error = null
                    )
                }else {
                    _detailState.value = _detailState.value.copy(
                        isLoading = false,
                        error = "Anime no enccontrado"
                    )
                }
            }catch (e: Exception) {
                _detailState.value = _detailState.value.copy(
                   isLoading = false,
                    error = e.message?: "Error al cargar los detalles"
                )
            }
        }
    }



    fun loadMore() {
        if(!_listState.value.isLoading && _listState.value.hasMoreData) {
            loadList(
               limit = _listState.value.limit ,
                offset = _listState.value.offset,
                loadMore = true
            )
        }

    }


}