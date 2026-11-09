package net.moviepumpkins.core.feature.media.application

import net.moviepumpkins.core.api.models.FeatureFilmDto
import net.moviepumpkins.core.api.models.MPARatingEnum
import net.moviepumpkins.core.api.models.MediaDto
import net.moviepumpkins.core.api.models.SeriesDto
import net.moviepumpkins.core.feature.media.FeatureFilm
import net.moviepumpkins.core.feature.media.Media
import net.moviepumpkins.core.feature.media.MediaFormat
import net.moviepumpkins.core.feature.media.Series

object MediaMapper {

    fun toResponse(media: Media): MediaDto {

        fun mapFeatureFilm(entity: FeatureFilm) = with(entity) {
            FeatureFilmDto(
                id = id!!,
                title = title,
                plot = plot,
                releaseYear = releaseYear,
                mpaRating = mpaRating?.let { MPARatingEnum.valueOf(it.name) },
                awardWinCount = awardWinCount,
                awardNominationCount = awardNominationCount,
                directors = directors,
                writers = writers,
                actors = actors,
                genres = genres,
                posterUrl = posterUrl,
                imdbId = imdbId,
                runtimeInMinutes = runtime,
                _tag = "FeatureFilm"
            )
        }

        fun mapSeries(entity: Series) = with(entity) {
            SeriesDto(
                id = id!!,
                title = title,
                plot = plot,
                releaseYear = releaseYear,
                mpaRating = mpaRating?.let { MPARatingEnum.valueOf(it.name) },
                awardWinCount = awardWinCount,
                awardNominationCount = awardNominationCount,
                directors = directors,
                writers = writers,
                actors = actors,
                genres = genres,
                posterUrl = posterUrl,
                imdbId = imdbId,
                numberOfSeasons = (media as Series).seasons,
                lastYear = lastYear,
                _tag = "Series"
            )
        }

        return when (media.format) {
            MediaFormat.FEATURE_FILM -> mapFeatureFilm(media as FeatureFilm)
            MediaFormat.SERIES -> mapSeries(media as Series)
        }
    }

}