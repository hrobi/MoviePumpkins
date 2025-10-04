package net.moviepumpkins.core.media.mediadetails.entity

sealed interface MediaTypeSpecificDetails

data class MovieDetails(
    val lengthInMinutes: Int
) : MediaTypeSpecificDetails

data class SeriesDetails(
    val seasonCount: Int?,
    val endYear: Int?
) : MediaTypeSpecificDetails