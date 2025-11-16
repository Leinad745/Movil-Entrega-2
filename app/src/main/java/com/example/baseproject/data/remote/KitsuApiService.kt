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

    @GET("anime")
    suspend fun fetchAnimeDetail(
        @Query("filter[id]") id : String
    ): AnimeListData

    @GET("anime")
    suspend fun fetchAnimesByName(
        @Query("filter[text]") name: String
    ): AnimeListData
}