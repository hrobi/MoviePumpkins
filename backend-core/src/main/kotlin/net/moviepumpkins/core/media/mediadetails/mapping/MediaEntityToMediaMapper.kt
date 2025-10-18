package net.moviepumpkins.core.media.mediadetails.mapping

import net.moviepumpkins.core.media.mediadetails.entity.MediaEntity
import net.moviepumpkins.core.media.mediadetails.entity.MediaType
import net.moviepumpkins.core.media.mediadetails.model.GenericMediaDetails
import net.moviepumpkins.core.media.mediadetails.model.Media

fun MediaEntity.toGenericMediaDetails() = GenericMediaDetails(
    id!!,
    title = title,
    shortDescription = shortDescription,
    directors = directors,
    writers = writers,
    actors = actors,
    originalTitle = originalTitle,
    countries = countries,
    awards = awards,
    awardsWinCount = awardsWinCount,
    nominationsCount = nominationsCount
)

fun MediaEntity.toMedia() = when (mediaType) {
    MediaType.MOVIE -> Media.Movie(
        genericMediaDetails = toGenericMediaDetails(),
        releaseYear = releaseYear,
        lengthInMinutes = otherDetails?.lengthInMinutes
    )

    MediaType.SERIES -> Media.Series(
        genericMediaDetails = toGenericMediaDetails(),
        seasons = otherDetails?.seasonCount,
        startedInYear = releaseYear,
        endedInYear = otherDetails?.endYear
    )
}