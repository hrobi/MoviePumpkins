package net.moviepumpkins.core.media.mediadetails

import io.zonky.test.db.AutoConfigureEmbeddedDatabase
import net.moviepumpkins.core.media.mediadetails.entity.MediaModificationRepository
import net.moviepumpkins.core.media.mediadetails.model.MovieModification
import net.moviepumpkins.core.util.Failure
import net.moviepumpkins.core.util.Success
import org.flywaydb.test.annotation.FlywayTest
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.api.assertInstanceOf
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.jdbc.Sql
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

private const val DUNE_ID: Long = 1

@SpringBootTest
@FlywayTest
@AutoConfigureEmbeddedDatabase
class MediaDetailsServiceTest {

    @Autowired
    lateinit var mediaModificationService: MediaModificationService

    @Autowired
    lateinit var mediaModificationRepository: MediaModificationRepository

    @Test
    @Sql("/test-mock/users.sql", "/test-mock/medias.sql")
    fun `create media modification for dune - should be ok`() {
        val createModificationResult = mediaModificationService.createModification(
            mediaId = DUNE_ID,
            mediaModification = MovieModification(
                username = "user7",
                title = "Dune: Part One",
                description = "Lorem Ipsum dolor sit amet",
                directors = null,
                writers = null,
                actors = listOf(),
                originalTitle = null,
                countries = null,
                awards = null,
                totalWins = -1,
                totalNominations = -1,
                releaseYear = 2021,
                lengthInMinutes = null
            )
        )

        assertInstanceOf<Success<Long>>(createModificationResult)

        val mediaModificationEntity =
            mediaModificationRepository.findById(createModificationResult.value)
                .orElse(null)

        assertAll(
            { assertNotNull(mediaModificationEntity) },
            { assertEquals(DUNE_ID, mediaModificationEntity.media.id) },
            { assertEquals("user7", mediaModificationEntity.user.username) },
            { assertEquals("Lorem Ipsum dolor sit amet", mediaModificationEntity.shortDescription) },
            { assertEquals("Dune: Part One", mediaModificationEntity.title) },
            { assertEquals(0, mediaModificationEntity.actors?.size ?: -1) },
            { assertEquals(-1, mediaModificationEntity.awardsWinCount) },
            { assertEquals(-1, mediaModificationEntity.nominationsCount) },
            { assertNull(mediaModificationEntity.directors, "mediaModificationEntity.directors should be null") },
            { assertNull(mediaModificationEntity.writers, "mediaModificationEntity.writers should be null") },
            {
                assertNull(
                    mediaModificationEntity.originalTitle,
                    "mediaModificationEntity.originalTitle should be null"
                )
            },
            { assertNull(mediaModificationEntity.countries, "mediaModificationEntity.countries should be null") },
            { assertNull(mediaModificationEntity.awards, "mediaModificationEntity.awards should be null") },
            { assertNull(mediaModificationEntity.otherDetails, "mediaModificationEntity.otherDetails should be null") },
        )
    }

    @Test
    fun `create media modification for dune - invalid modification`() {
        val createModificationResult = mediaModificationService.createModification(
            mediaId = DUNE_ID,
            mediaModification = MovieModification(
                username = "user7",
                title = "Du",
                description = "Lorem Ipsum",
                directors = listOf("a"),
                writers = listOf("b"),
                actors = listOf("c"),
                originalTitle = "Du",
                countries = listOf("us"),
                awards = listOf("some"),
                totalWins = 9_000_000,
                totalNominations = -5,
                releaseYear = 3021,
                lengthInMinutes = -5
            )
        )

        assertInstanceOf<Failure<MediaModificationError.ViolatedConstraints>>(createModificationResult)
        val invalidDataPaths = createModificationResult.cause.validationResult.errors.map {
            it.dataPath
        }

        assertAll(
            { assertContains(invalidDataPaths, ".title") },
            { assertContains(invalidDataPaths, ".description") },
            { assertContains(invalidDataPaths, ".directors[0]") },
            { assertContains(invalidDataPaths, ".writers[0]") },
            { assertContains(invalidDataPaths, ".actors[0]") },
            { assertContains(invalidDataPaths, ".originalTitle") },
            { assertContains(invalidDataPaths, ".countries[0]") },
            { assertContains(invalidDataPaths, ".awards[0]") },
            { assertContains(invalidDataPaths, ".totalWins") },
            { assertContains(invalidDataPaths, ".totalNominations") },
            { assertContains(invalidDataPaths, ".releaseYear") },
            { assertContains(invalidDataPaths, ".lengthInMinutes") },
        )
    }

    @Test
    fun `create media modification for dune - media doesn't exist`() {
        val createModificationResult = mediaModificationService.createModification(
            mediaId = -1,
            mediaModification = MovieModification(
                username = "user7",
                title = "Dune: Part One",
                description = "Lorem Ipsum dolor sit amet",
                directors = null,
                writers = null,
                actors = listOf(),
                originalTitle = null,
                countries = null,
                awards = null,
                totalWins = -1,
                totalNominations = -1,
                releaseYear = 2021,
                lengthInMinutes = null
            )
        )

        assertInstanceOf<Failure<MediaModificationError.MediaDoesNotExist>>(createModificationResult)
        assertInstanceOf<MediaModificationError.MediaDoesNotExist>(createModificationResult.cause)
    }

    @Test
    @Sql("/test-mock/users.sql", "/test-mock/medias.sql")
    fun `create media modification for dune - modification already exists`() {
        val createModificationResult01 = mediaModificationService.createModification(
            mediaId = DUNE_ID,
            mediaModification = MovieModification(
                username = "user7",
                title = "Dune: Part One",
                description = "Lorem Ipsum dolor sit amet",
                directors = null,
                writers = null,
                actors = listOf(),
                originalTitle = null,
                countries = null,
                awards = null,
                totalWins = -1,
                totalNominations = -1,
                releaseYear = 2021,
                lengthInMinutes = null
            )
        )

        val createModificationResult02 = mediaModificationService.createModification(
            mediaId = DUNE_ID,
            mediaModification = MovieModification(
                username = "user7",
                title = "Dune: Part One",
                description = "Lorem Ipsum dolor sit amet",
                directors = null,
                writers = null,
                actors = listOf(),
                originalTitle = null,
                countries = null,
                awards = null,
                totalWins = -1,
                totalNominations = -1,
                releaseYear = 2021,
                lengthInMinutes = null
            )
        )

        assertInstanceOf<Success<Long>>(createModificationResult01)
        assertInstanceOf<Failure<MediaModificationError.ModificationAlreadyExists>>(createModificationResult02)
        assertInstanceOf<MediaModificationError.ModificationAlreadyExists>(createModificationResult02.cause)
    }
}