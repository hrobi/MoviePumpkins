package net.moviepumpkins.core.feature.media.application.mapper

import net.moviepumpkins.core.api.models.FeatureFilmDto
import net.moviepumpkins.core.api.models.MPARatingEnum
import net.moviepumpkins.core.api.models.SeriesDto
import net.moviepumpkins.core.feature.media.domain.model.entity.MediaEntity
import net.moviepumpkins.core.feature.media.domain.model.value.MediaFormatData

object MediaMapper {

    fun mapEntityToDto(mediaEntity: MediaEntity) =
        with(mediaEntity) {
            when (val format = mediaEntity.format) {
                is MediaFormatData.FeatureFilm -> FeatureFilmDto(
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
                    runtimeInMinutes = format.runtimeInMinutes,
                    _tag = "FeatureFilm"
                )

                is MediaFormatData.Series -> SeriesDto(
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
                    numberOfSeasons = format.numberOfSeasons,
                    lastYear = format.lastYear,
                    _tag = "Series"
                )
            }
        }

}