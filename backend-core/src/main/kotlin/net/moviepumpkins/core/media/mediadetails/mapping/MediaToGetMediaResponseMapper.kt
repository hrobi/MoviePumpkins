package net.moviepumpkins.core.media.mediadetails.mapping

import net.moviepumpkins.core.integration.models.GetMovieResponse
import net.moviepumpkins.core.integration.models.GetSeriesResponse
import net.moviepumpkins.core.media.mediadetails.model.Media

fun Media.toGetMediaResponse(withPosterLink: String? = null) = when (this) {
    is Media.Movie -> toGetMovieResponse(withPosterLink)
    is Media.Series -> toGetSeriesResponse(withPosterLink)
}

fun Media.Movie.toGetMovieResponse(withPosterLink: String? = null) = GetMovieResponse(
    id = id,
    title = title,
    description = shortDescription,
    directors = directors,
    writers = writers,
    actors = actors,
    originalTitle = originalTitle,
    countries = countries,
    awards = awards,
    totalWins = awardsWinCount,
    totalNominations = nominationsCount,
    releaseYear = releaseYear,
    lengthInMinutes = lengthInMinutes,
    posterLink = withPosterLink
)

fun Media.Series.toGetSeriesResponse(withPosterLink: String? = null) = GetSeriesResponse(
    id = id,
    title = title,
    description = shortDescription,
    directors = directors,
    writers = writers,
    actors = actors,
    originalTitle = originalTitle,
    countries = countries,
    awards = awards,
    totalWins = awardsWinCount,
    totalNominations = nominationsCount,
    seasons = seasons,
    startedInYear = startedInYear,
    endedInYear = endedInYear,
    posterLink = withPosterLink
)