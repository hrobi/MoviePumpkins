package net.moviepumpkins.core.media.mediadetails.model

sealed class MediaModification(
    open val username: String,
    open val title: String?,
    open val description: String?,
    open val directors: List<String>?,
    open val writers: List<String>?,
    open val actors: List<String>?,
    open val originalTitle: String?,
    open val countries: List<String>?,
    open val awards: List<String>?,
    open val totalWins: Int?,
    open val totalNominations: Int?,
)

data class MovieModification(
    override val username: String,
    override val title: String?,
    override val description: String?,
    override val directors: List<String>?,
    override val writers: List<String>?,
    override val actors: List<String>?,
    override val originalTitle: String?,
    override val countries: List<String>?,
    override val awards: List<String>?,
    override val totalWins: Int?,
    override val totalNominations: Int?,
    val releaseYear: Int?,
    val lengthInMinutes: Int?,
) : MediaModification(
    username = username,
    title = title,
    description = description,
    directors = directors,
    writers = writers,
    actors = actors,
    originalTitle = originalTitle,
    countries = countries,
    awards = awards,
    totalWins = totalWins,
    totalNominations = totalNominations
)

data class SeriesModification(
    override val username: String,
    override val title: String?,
    override val description: String?,
    override val directors: List<String>?,
    override val writers: List<String>?,
    override val actors: List<String>?,
    override val originalTitle: String?,
    override val countries: List<String>?,
    override val awards: List<String>?,
    override val totalWins: Int?,
    override val totalNominations: Int?,
    val seasons: Int?,
    val startedInYear: Int?,
    val endedInYear: Int?,
) : MediaModification(
    username = username,
    title = title,
    description = description,
    directors = directors,
    writers = writers,
    actors = actors,
    originalTitle = originalTitle,
    countries = countries,
    awards = awards,
    totalWins = totalWins,
    totalNominations = totalNominations
)