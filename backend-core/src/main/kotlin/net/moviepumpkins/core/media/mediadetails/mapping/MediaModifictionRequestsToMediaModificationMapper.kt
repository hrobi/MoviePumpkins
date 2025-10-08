package net.moviepumpkins.core.media.mediadetails.mapping

import net.moviepumpkins.core.integration.models.CreateMediaModificationRequest
import net.moviepumpkins.core.integration.models.CreateMovieModificationRequest
import net.moviepumpkins.core.integration.models.CreateSeriesModificationRequest
import net.moviepumpkins.core.integration.models.UpdateMediaModificationRequest
import net.moviepumpkins.core.integration.models.UpdateMovieModificationRequest
import net.moviepumpkins.core.integration.models.UpdateSeriesModificationRequest
import net.moviepumpkins.core.media.mediadetails.model.MovieModification
import net.moviepumpkins.core.media.mediadetails.model.SeriesModification

fun CreateMovieModificationRequest.toMovieModification(username: String) = MovieModification(
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
    totalNominations = totalNominations,
    releaseYear = releaseYear,
    lengthInMinutes = lengthInMinutes
)

fun CreateSeriesModificationRequest.toSeriesModification(username: String) = SeriesModification(
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
    totalNominations = totalNominations,
    seasons = seasons,
    startedInYear = startedInYear,
    endedInYear = endedInYear
)

fun CreateMediaModificationRequest.toMediaModification(username: String) = when (this) {
    is CreateMovieModificationRequest -> toMovieModification(username)
    is CreateSeriesModificationRequest -> toSeriesModification(username)
}

fun UpdateMovieModificationRequest.toMovieModification(username: String) = MovieModification(
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
    totalNominations = totalNominations,
    releaseYear = releaseYear,
    lengthInMinutes = lengthInMinutes
)

fun UpdateSeriesModificationRequest.toSeriesModification(username: String) = SeriesModification(
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
    totalNominations = totalNominations,
    seasons = seasons,
    startedInYear = startedInYear,
    endedInYear = endedInYear
)

fun UpdateMediaModificationRequest.toMediaModification(username: String) = when (this) {
    is UpdateMovieModificationRequest -> toMovieModification(username)
    is UpdateSeriesModificationRequest -> toSeriesModification(username)
}