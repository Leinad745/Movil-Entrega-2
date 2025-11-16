package com.example.animeping.viewmodel

import com.example.animeping.data.AnimeRepository
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import com.example.animeping.data.UserRepository
import com.example.animeping.model.Anime
import android.app.Application
import androidx.lifecycle.AndroidViewModel

data class AnimeListUiState(
    val animes: List<Anime> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val limit: Int = 20,
    val offset: Int = 0,
    val hasMoreData: Boolean = true
)

data class AnimeDetailState(
    val anime: Anime? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
)

data class searchState(
    val animes: List<Anime> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
)

class AnimeViewModel(application: Application) : AndroidViewModel(application){

    private val repository: AnimeRepository = AnimeRepository()
    private val userRepository: UserRepository = UserRepository.getInstance(application)


    //lista de animes
    private val _listState = MutableStateFlow(AnimeListUiState())
    var listState: StateFlow<AnimeListUiState> = _listState.asStateFlow()
        private set

    //detalles de anime
    private val _detailState = MutableStateFlow(AnimeDetailState())
    var detailState: StateFlow<AnimeDetailState> = _detailState.asStateFlow()

    //anime por nombre
    private val _searchState = MutableStateFlow(searchState())
    val searchState: StateFlow<searchState> = _searchState.asStateFlow()

    fun toggleFavorite(animeId: String) {
        viewModelScope.launch {
            val currentFavorites = userRepository.getFavorites()
            if (currentFavorites.contains(animeId)) {
                userRepository.removeFavorite(animeId)
                println("Eliminado de favoritos: $animeId")
            } else {
                userRepository.addFavorite(animeId)
                println("Añadido a favoritos: $animeId")
            }
        }
    }

    fun isFavorite(animeId: String): Boolean {
        return userRepository.getFavorites().contains(animeId)
    }

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

    fun searchAnimeByName(query: String) {
        _searchState.value = searchState(isLoading = true, error = null)
        viewModelScope.launch {
            try {
                val result = repository.getAnimeByAnime(query)
                val foundAnimes = result.data ?: emptyList()
                _searchState.value = searchState(
                    animes = foundAnimes,
                    isLoading = false,
                    error = null
                )
            } catch (e: Exception) {
                e.printStackTrace()
                _searchState.value = searchState(
                    isLoading = false,
                    error = e.message ?: "Error en la búsqueda"
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