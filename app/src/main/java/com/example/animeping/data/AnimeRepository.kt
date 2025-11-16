package com.example.animeping.data

import com.example.animeping.data.remote.KitsuApiService
import com.example.animeping.data.remote.NetworkModule
import com.example.animeping.model.Anime
import com.example.animeping.model.AnimeListData

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

}