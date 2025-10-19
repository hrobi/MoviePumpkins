package net.moviepumpkins.core.media.mediadetails

import io.zonky.test.db.AutoConfigureEmbeddedDatabase
import net.moviepumpkins.core.app.model.UserRole
import net.moviepumpkins.core.media.mediadetails.entity.MediaModificationRepository
import net.moviepumpkins.core.media.mediadetails.entity.MediaRepository
import net.moviepumpkins.core.media.mediadetails.model.ErrorRemovingMediaModification
import net.moviepumpkins.core.media.mediadetails.model.InvalidMediaModificationError
import net.moviepumpkins.core.media.mediadetails.model.MediaDoesNotExistError
import net.moviepumpkins.core.media.mediadetails.model.ModificationAlreadyExistsError
import net.moviepumpkins.core.media.mediadetails.model.MovieModification
import net.moviepumpkins.core.media.mediadetails.model.UserDoesNotMatchError
import net.moviepumpkins.core.media.mediadetails.service.MediaModificationService
import net.moviepumpkins.core.util.result.Failure
import net.moviepumpkins.core.util.result.Success
import net.moviepumpkins.core.util.result.succeedOrElse
import net.moviepumpkins.core.util.userAccount
import org.flywaydb.test.annotation.FlywayTest
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.api.assertInstanceOf
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.jdbc.Sql
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull

@SpringBootTest
@FlywayTest
@AutoConfigureEmbeddedDatabase
class MediaModificationServiceTest {

    @Autowired
    lateinit var mediaModificationService: MediaModificationService

    @Autowired
    lateinit var mediaModificationRepository: MediaModificationRepository

    @Autowired
    lateinit var mediaRepository: MediaRepository

    companion object {
        val duneValidMediaModification = MovieModification(
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
    }

    @Test
    @Sql("/test-mock/users.sql", "/test-mock/medias.sql")
    fun `create and update media modification for dune - should be ok`() {

        val duneId = mediaRepository.getIdByTitleOrNull("Dune: Part One")!!

        val createModificationResult = mediaModificationService.createModification(
            mediaId = duneId,
            mediaModification = duneValidMediaModification
        )

        assertInstanceOf<Success<Long>>(createModificationResult)

        val mediaModificationEntity =
            mediaModificationRepository.findById(createModificationResult.value)
                .orElse(null)

        assertAll(
            { assertNotNull(mediaModificationEntity) },
            { assertEquals(duneId, mediaModificationEntity.media.id) },
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

        val updateModificationResult = mediaModificationService.updateModification(
            mediaModificationId = createModificationResult.value,
            mediaModification = duneValidMediaModification.copy(directors = listOf("Dennis Villeneuve"))
        )

        assertInstanceOf<Success<Unit>>(updateModificationResult)

        val directors = mediaModificationRepository.findById(createModificationResult.value).orElse(null).directors
        assertEquals(listOf("Dennis Villeneuve"), directors)
    }

    @Test
    @Sql("/test-mock/users.sql", "/test-mock/medias.sql")
    fun `create media modification for dune - invalid modification`() {
        val duneId = mediaRepository.getIdByTitleOrNull("Dune: Part One")!!

        val createModificationResult = mediaModificationService.createModification(
            mediaId = duneId,
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

        assertInstanceOf<Failure<InvalidMediaModificationError>>(createModificationResult)
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
            mediaModification = duneValidMediaModification
        )

        assertInstanceOf<Failure<MediaDoesNotExistError>>(createModificationResult)
        assertInstanceOf<MediaDoesNotExistError>(createModificationResult.cause)
    }

    @Test
    @Sql("/test-mock/users.sql", "/test-mock/medias.sql")
    fun `create media modification for dune - modification already exists`() {

        val duneId = mediaRepository.getIdByTitleOrNull("Dune: Part One")!!


        val createModificationResult01 = mediaModificationService.createModification(
            mediaId = duneId,
            mediaModification = duneValidMediaModification
        )

        val createModificationResult02 = mediaModificationService.createModification(
            mediaId = duneId,
            mediaModification = duneValidMediaModification
        )

        assertInstanceOf<Success<Long>>(createModificationResult01)
        assertInstanceOf<Failure<ModificationAlreadyExistsError>>(createModificationResult02)
        assertInstanceOf<ModificationAlreadyExistsError>(createModificationResult02.cause)
    }

    @Test
    fun `delete modification for dune - success`() {

        val duneId = mediaRepository.getIdByTitleOrNull("Dune: Part One")!!


        val id = mediaModificationService.createModification(
            mediaId = duneId,
            mediaModification = duneValidMediaModification
        )
            .succeedOrElse { throw IllegalStateException("Check `create and update media modification for dune - should be ok`") }

        mediaModificationService.removeModification(id, remover = userAccount("user8", role = UserRole.ADMIN))

        assertFalse(mediaModificationRepository.existsById(id), "modification should be deleted")
    }

    @Test
    fun `delete modification for dune - invalid user`() {

        val duneId = mediaRepository.getIdByTitleOrNull("Dune: Part One")!!

        val id = mediaModificationService.createModification(
            mediaId = duneId,
            mediaModification = duneValidMediaModification
        )
            .succeedOrElse { throw IllegalStateException("Check `create and update media modification for dune - should be ok`") }

        val removeModificationResult =
            mediaModificationService.removeModification(id, remover = userAccount("user8", role = UserRole.REVIEWER))

        assertInstanceOf<Failure<ErrorRemovingMediaModification>>(removeModificationResult)
        assertInstanceOf<UserDoesNotMatchError>(removeModificationResult.cause)
    }
}