package net.moviepumpkins.core.media.mediadetails.mapping

import net.moviepumpkins.core.media.mediadetails.entity.MediaEntity
import net.moviepumpkins.core.media.mediadetails.entity.MediaModificationEntity
import net.moviepumpkins.core.media.mediadetails.entity.MediaType
import net.moviepumpkins.core.media.mediadetails.entity.MediaTypeSpecificDetails
import net.moviepumpkins.core.media.mediadetails.model.MediaModification
import net.moviepumpkins.core.media.mediadetails.model.MovieModification
import net.moviepumpkins.core.media.mediadetails.model.SeriesModification
import net.moviepumpkins.core.user.entity.UserAccountEntity

fun MediaModification.toMediaTypeSpecificDetails(): MediaTypeSpecificDetails? = when (this) {
    is MovieModification -> MediaTypeSpecificDetails(
        lengthInMinutes = lengthInMinutes,
        seasonCount = null,
        endYear = null
    )

    is SeriesModification -> MediaTypeSpecificDetails(
        lengthInMinutes = null,
        seasonCount = seasons,
        endYear = endedInYear
    )
}.toNullable()

fun MediaModification.toMediaModificationEntity(
    mediaEntity: MediaEntity,
    userAccountEntity: UserAccountEntity,
    posterFile: String?,
) =
    MediaModificationEntity(
        media = mediaEntity,
        user = userAccountEntity,
        title = title,
        shortDescription = description,
        releaseYear = when (this) {
            is MovieModification -> releaseYear
            is SeriesModification -> startedInYear
        },
        directors = directors,
        writers = writers,
        actors = actors,
        originalTitle = originalTitle,
        countries = countries,
        awards = awards,
        awardsWinCount = totalWins,
        nominationsCount = totalNominations,
        posterFile = posterFile,
        otherDetails = toMediaTypeSpecificDetails(),
        mediaType = when (this) {
            is MovieModification -> MediaType.MOVIE
            is SeriesModification -> MediaType.SERIES
        }
    )