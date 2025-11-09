package com.example.baseproject.model

data class Anime (
    val name : String,
    val url : String
    )

data class AnimeListData
    (val data : List<Anime>)

data class AnimeDetail (
    val name : String,
    val synopsis : String,
    var status : String,
    val posterImage : String,
    var episodeCount : Int,
    var averageRating : Double,
    val genre : String
)