package com.example.baseproject.data

import com.example.baseproject.data.remote.KitsuApiService
import com.example.baseproject.data.remote.NetworkModule
import com.example.baseproject.model.Anime
import com.example.baseproject.model.AnimeListData

class AnimeRepository
    (private val api: KitsuApiService = NetworkModule.api){

    suspend fun getAnimeList(limit: Int = 20, offset: Int = 0): AnimeListData {
        return api.fetchAnimeList(limit, offset)
    }

    suspend fun getAnimeById(animeId: String): Anime? {
        try {
            val response = api.fetchAnimeDetail(id = animeId)
            return response.data?.firstOrNull()

        }catch (e: Exception) {
            throw e
        }
    }
    suspend fun getAnimeByAnime(name: String): AnimeListData {
        return api.fetchAnimesByName(name)
    }

}