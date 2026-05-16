package net.moviepumpkins.core.feature.media.domain.mapper

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class OmdbDtoPropertyMapperTest {

    @Test
    fun `test NA value`() {
        Assertions.assertAll(
            { Assertions.assertEquals(null, OmdbMediaDtoPropertyParser.handleNA("N/A")) },
            { Assertions.assertEquals("Hello World!", OmdbMediaDtoPropertyParser.handleNA("Hello World!")) },
        )
    }

    @Test
    fun `test extracting wins and nominations`() {
        Assertions.assertAll(
            {
                Assertions.assertEquals(
                    null to null,
                    OmdbMediaDtoPropertyParser.extractWinsAndNominations("N/A"),
                )
            },
            {
                Assertions.assertEquals(
                    41 to 13,
                    OmdbMediaDtoPropertyParser.extractWinsAndNominations("Won 8 Primetime Emmys. 41 wins & 13 nominations total")
                )
            },
            {
                Assertions.assertEquals(
                    null to 13,
                    OmdbMediaDtoPropertyParser.extractWinsAndNominations("Won 8 Primetime Emmys. 13 nominations total"),
                )
            })
    }

    @Test
    fun `test extracting start and end years`() {
        Assertions.assertAll(
            { Assertions.assertEquals(null to null, OmdbMediaDtoPropertyParser.extractReleaseYearAndEndYear("N/A")) },
            {
                Assertions.assertEquals(
                    2018 to 2022,
                    OmdbMediaDtoPropertyParser.extractReleaseYearAndEndYear("2018-2022")
                )
            },
            { Assertions.assertEquals(2005 to null, OmdbMediaDtoPropertyParser.extractReleaseYearAndEndYear("2005")) },
        )
    }

    @Test
    fun `test extracting runtime`() {
        Assertions.assertEquals(124, OmdbMediaDtoPropertyParser.extractRuntimeInMinutes("124 min"))
    }

}