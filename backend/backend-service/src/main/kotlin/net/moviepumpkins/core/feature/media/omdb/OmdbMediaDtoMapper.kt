package net.moviepumpkins.core.feature.media.omdb

import net.moviepumpkins.core.application.integration.omdb.dto.OmdbMediaDto
import net.moviepumpkins.core.feature.media.FeatureFilm
import net.moviepumpkins.core.feature.media.MPARating
import net.moviepumpkins.core.feature.media.Media
import net.moviepumpkins.core.feature.media.Series
import net.moviepumpkins.core.shared.logger

object OmdbMediaDtoMapper {

    fun mapToMediaEntity(omdbMediaDto: OmdbMediaDto): Media {
        val (wins, nominations) = OmdbMediaDtoPropertyParser.extractWinsAndNominations(omdbMediaDto.awards)
        val (releaseYear, endYear) = OmdbMediaDtoPropertyParser.extractReleaseYearAndEndYear(omdbMediaDto.year)
        val commonDetails = Media.CommonDetails(
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
            imdbId = omdbMediaDto.imdbId,
        )
        return when (omdbMediaDto.type) {
            "series" -> Series(
                commonDetails,
                endYear,
                omdbMediaDto.totalSeasons?.handleNA()?.toInt()
            )

            "movie" -> FeatureFilm(
                commonDetails,
                OmdbMediaDtoPropertyParser.extractRuntimeInMinutes(omdbMediaDto.runtime)
            )

            else -> {
                logger().error("Cannot map omdb response to entity. Unsupported omdb type ${omdbMediaDto.type}")
                throw IllegalArgumentException("unknown omdb type")
            }
        }
    }

    private fun String.handleNA(): String? = OmdbMediaDtoPropertyParser.handleNA(this)

}