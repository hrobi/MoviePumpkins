package net.moviepumpkins.core.media.mediadetails.model

interface AbstractGenericMediaDetails {
    val id: Long
    val title: String
    val shortDescription: String
    val directors: List<String>?
    val writers: List<String>?
    val actors: List<String>?
    val originalTitle: String?
    val countries: List<String>?
    val awards: List<String>?
    val awardsWinCount: Int?
    val nominationsCount: Int?
}

data class GenericMediaDetails(
    override val id: Long,
    override val title: String,
    override val shortDescription: String,
    override val directors: List<String>?,
    override val writers: List<String>?,
    override val actors: List<String>?,
    override val originalTitle: String?,
    override val countries: List<String>?,
    override val awards: List<String>?,
    override val awardsWinCount: Int?,
    override val nominationsCount: Int?,
) : AbstractGenericMediaDetails

sealed interface Media : AbstractGenericMediaDetails {

    data class Series(
        val genericMediaDetails: GenericMediaDetails,
        val seasons: Int?,
        val startedInYear: Int,
        val endedInYear: Int?,
    ) : AbstractGenericMediaDetails by genericMediaDetails, Media

    data class Movie(
        val genericMediaDetails: GenericMediaDetails,
        val releaseYear: Int,
        val lengthInMinutes: Int?,
    ) : AbstractGenericMediaDetails by genericMediaDetails, Media
}