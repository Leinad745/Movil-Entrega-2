package com.example.baseproject.model

data class Anime (
    val id : String,
    val type: String,
    val attributes: AnimeAttributes
)

data class AnimeListData
    (val data : List<Anime>)

data class AnimeAttributes(
    val description: String,
    val canonicalTitle: String,
    var status : String,
    var episodeCount : Int,
    var ageRating: String?,
    val userCount: Int,
    val startDate: String,
    val endDate: String,
    val nextRelease: String?,
    val posterImage: PosterImage
)

data class PosterImage(
    val tiny: String,
    val large: String,
    val small: String,
    val medium: String,
    val original: String,
)


