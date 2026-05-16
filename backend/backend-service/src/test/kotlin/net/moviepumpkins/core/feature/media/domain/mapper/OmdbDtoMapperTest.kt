package net.moviepumpkins.core.feature.media.domain.mapper

import net.moviepumpkins.core.feature.media.domain.model.value.MPARating
import net.moviepumpkins.core.feature.media.domain.model.value.MediaFormatData
import net.moviepumpkins.core.integration.omdb.dto.OmdbMediaDto
import net.moviepumpkins.core.integration.omdb.dto.OmdbRatingDto
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class OmdbDtoMapperTest {

    private val arcaneDto = OmdbMediaDto(
        title = "Arcane",
        year = "2021–2024",
        rated = "TV-14",
        released = "06 Nov 2021",
        runtime = "N/A",
        genre = "Animation, Action, Adventure",
        director = "N/A",
        writer = "N/A",
        actors = "Kevin Alejandro, Hailee Steinfeld, Ella Purnell",
        plot = "Amid the stark discord of twin cities Piltover and Zaun, two sisters fight on rival sides of a war between magic technologies and clashing convictions.",
        language = "English, French",
        country = "United States, France",
        awards = "Won 8 Primetime Emmys. 41 wins & 13 nominations total",
        poster = "https://m.media-amazon.com/images/M/MV5BYjA2NzhlMDItNWRmZC00MzRjLWE3ZjAtZjBlZDAwOWY2ODdjXkEyXkFqcGc@._V1_QL75_UX380_CR0,0,380,562_.jpg",
        ratings = listOf(OmdbRatingDto(source = "Internet Movie Database", value = "9.0/10")),
        metascore = "N/A",
        imdbRating = "9.0",
        imdbVotes = "441,347",
        imdbId = "tt11126994",
        type = "series",
        totalSeasons = "2",
        response = "True",
    )

    @Test
    fun `maps Arcane OMDB dto to media entity`() {
        val result = OmdbMediaDtoMapper.mapToMediaEntity(arcaneDto)

        Assertions.assertAll(
            { Assertions.assertEquals("Arcane", result.title) },
            {
                Assertions.assertEquals(
                    "Amid the stark discord of twin cities Piltover and Zaun, two sisters fight on rival sides of a war between magic technologies and clashing convictions.",
                    result.plot
                )
            },
            { Assertions.assertEquals(2021, result.releaseYear) },
            { Assertions.assertEquals(MPARating.PG_13, result.mpaRating) },
            { Assertions.assertEquals(41, result.awardWinCount) },
            { Assertions.assertEquals(13, result.awardNominationCount) },
            { Assertions.assertNull(result.directors) },
            { Assertions.assertNull(result.writers) },
            { Assertions.assertEquals(listOf("Kevin Alejandro", "Hailee Steinfeld", "Ella Purnell"), result.actors) },
            { Assertions.assertEquals(listOf("Animation", "Action", "Adventure"), result.genres) },
            {
                Assertions.assertEquals(
                    "https://m.media-amazon.com/images/M/MV5BYjA2NzhlMDItNWRmZC00MzRjLWE3ZjAtZjBlZDAwOWY2ODdjXkEyXkFqcGc@._V1_QL75_UX380_CR0,0,380,562_.jpg",
                    result.posterUrl
                )
            },
            { Assertions.assertEquals(MediaFormatData.Series(numberOfSeasons = 2, lastYear = 2024), result.format) },
            { Assertions.assertEquals("tt11126994", result.imdbId) },
        )
    }

}
