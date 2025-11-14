package com.example.baseproject.data.remote

import com.example.baseproject.model.*
import retrofit2.http.GET
import retrofit2.http.Query


interface KitsuApiService {
    @GET("anime")
    suspend fun fetchAnimeList(
        @Query("page[limit]") limit : Int = 20,
        @Query("page[offset]") offset : Int = 0

    ): AnimeListData

    @GET("id")
    suspend fun fetchAnimeDetail(
        @Query("id") id : String
    ): Anime

}