package net.moviepumpkins.core.feature.media

import net.moviepumpkins.core.api.models.FeatureFilmDto
import net.moviepumpkins.core.api.models.MPARatingEnum
import net.moviepumpkins.core.api.models.MediaDto
import net.moviepumpkins.core.api.models.SeriesDto

object MediaMapper {

    fun mapEntityToDto(mediaEntity: MediaEntity): MediaDto {

        fun mapFeatureFilm(entity: FeatureFilmEntity) = with(entity) {
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

        fun mapSeries(entity: SeriesEntity) = with(entity) {
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
                numberOfSeasons = (mediaEntity as SeriesEntity).seasons,
                lastYear = lastYear,
                _tag = "Series"
            )
        }

        return when (mediaEntity.format) {
            MediaFormat.FEATURE_FILM -> mapFeatureFilm(mediaEntity as FeatureFilmEntity)
            MediaFormat.SERIES -> mapSeries(mediaEntity as SeriesEntity)
        }
    }

}