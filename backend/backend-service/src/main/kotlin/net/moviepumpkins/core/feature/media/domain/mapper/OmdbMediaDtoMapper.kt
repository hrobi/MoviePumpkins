package net.moviepumpkins.core.feature.media.domain.mapper

import net.moviepumpkins.core.feature.media.domain.model.entity.MediaEntity
import net.moviepumpkins.core.feature.media.domain.model.value.MPARating
import net.moviepumpkins.core.feature.media.domain.model.value.MediaFormatData
import net.moviepumpkins.core.integration.omdb.dto.OmdbMediaDto

object OmdbMediaDtoMapper {

    fun mapToMediaEntity(omdbMediaDto: OmdbMediaDto): MediaEntity {
        val (wins, nominations) = OmdbMediaDtoPropertyParser.extractWinsAndNominations(omdbMediaDto.awards)
        val (releaseYear, endYear) = OmdbMediaDtoPropertyParser.extractReleaseYearAndEndYear(omdbMediaDto.year)
        return MediaEntity(
            title = omdbMediaDto.title.takeIf { it != "N/A" }
                ?: throw IllegalArgumentException("title of media must be known!"),
            plot = omdbMediaDto.plot.handleNA(),
            releaseYear = releaseYear,
            mpaRating = omdbMediaDto.rated.handleNA()?.let {
                MPARating.parseOrNull(it) ?: throw IllegalStateException("Unknown rating")
            },
            awardWinCount = wins,
            awardNominationCount = nominations,
            directors = omdbMediaDto.director.handleNA()?.split(", "),
            writers = omdbMediaDto.writer.handleNA()?.split(", "),
            actors = omdbMediaDto.actors.handleNA()?.split(", "),
            genres = omdbMediaDto.genre.handleNA()?.split(", "),
            posterUrl = omdbMediaDto.poster.handleNA(),
            format = when (omdbMediaDto.type) {
                "series" -> MediaFormatData.Series(
                    numberOfSeasons = omdbMediaDto.totalSeasons?.handleNA()?.toInt(),
                    lastYear = endYear
                )

                "movie" -> MediaFormatData.FeatureFilm(
                    runtimeInMinutes = OmdbMediaDtoPropertyParser.extractRuntimeInMinutes(omdbMediaDto.runtime)
                )

                else -> throw IllegalStateException("""Unknown type for media (should be either "series" or "movie")""")
            },
            imdbId = omdbMediaDto.imdbId
        )
    }

    private fun String.handleNA(): String? = OmdbMediaDtoPropertyParser.handleNA(this)

}