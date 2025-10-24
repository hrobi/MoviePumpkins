package net.moviepumpkins.core.media.scores

import io.zonky.test.db.AutoConfigureEmbeddedDatabase
import net.moviepumpkins.core.TestHelperService
import net.moviepumpkins.core.flavour.service.FlavoursSetupService
import net.moviepumpkins.core.flavour.service.ReadFlavoursFromFileService
import net.moviepumpkins.core.media.scores.model.FlavourDoesNotExistError
import net.moviepumpkins.core.media.scores.model.InvalidScoreError
import net.moviepumpkins.core.media.scores.model.MediaDoesNotExistError
import net.moviepumpkins.core.media.scores.model.MediaScore
import net.moviepumpkins.core.util.result.Success
import net.moviepumpkins.core.util.result.succeedOrElse
import org.flywaydb.test.annotation.FlywayTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.api.assertInstanceOf
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.DefaultApplicationArguments
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.jdbc.Sql
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

@SpringBootTest
@FlywayTest
@AutoConfigureEmbeddedDatabase
@Sql("/test-mock/users.sql", "/test-mock/medias.sql")
class ScoreServiceTest {

    @Autowired
    lateinit var flavoursSetupService: FlavoursSetupService

    @Autowired
    lateinit var scoreService: ScoreService

    @Autowired
    lateinit var readFlavoursFromFileService: ReadFlavoursFromFileService

    @Autowired
    lateinit var testHelperService: TestHelperService

    @BeforeEach
    fun setup() {
        flavoursSetupService.run(DefaultApplicationArguments())
    }

    @Test
    fun `get all flavours - success`() {
        val flavoursFromFile = readFlavoursFromFileService.readAllFlavours()
        val flavours = scoreService.getAllFlavours()
        assertEquals(flavoursFromFile, flavours)
    }

    data class ComparableTestMediaScore(
        val flavourId: String,
        val score: Float,
        val count: Int,
    )

    private fun MediaScore.toComparableTestMediaScore() = ComparableTestMediaScore(
        flavourId = flavour.id,
        score = "%.2f".format(score).toFloat(),
        count = count
    )

    companion object {
        val expectedMediaScoresForFlow_GEN2 = ComparableTestMediaScore(
            flavourId = "GEN2",
            score = 4.83f,
            count = 3
        )

        val expectedMediaScoresForFlow_THR1 = ComparableTestMediaScore(
            flavourId = "THR1",
            score = 4.0f,
            count = 2
        )

        val expectedMediaScoresForFlow_GEN4 = ComparableTestMediaScore(
            flavourId = "GEN4",
            score = 5.0f,
            count = 1
        )

        val expectedMediaScoresForFlow_EMO3 = ComparableTestMediaScore(
            flavourId = "EMO3",
            score = 3.0f,
            count = 1
        )

        val expectedMediaScoreForFlow_byUser7_THR1 = ComparableTestMediaScore(
            flavourId = "THR1",
            score = 4.0f,
            count = 1
        )

        val expectedMediaScoreForFlow_byUser7_GEN2 = ComparableTestMediaScore(
            flavourId = "GEN2",
            score = 5.0f,
            count = 1
        )

        val expectedMediaScoreForFlow_byUser7_GEN4 = ComparableTestMediaScore(
            flavourId = "GEN4",
            score = 5.0f,
            count = 1
        )

        val expectedMediaScoreForFlow_byUser7_EMO3 = ComparableTestMediaScore(
            flavourId = "EMO3",
            score = 3.0f,
            count = 1
        )

    }

    @Test
    fun `rate flow - success`() {
        val flowId = testHelperService.getFlowId()
        assertNull(scoreService.saveMediaScore(flowId, "THR1", 4.0f, "user7"))
        assertNull(scoreService.saveMediaScore(flowId, "GEN2", 5.0f, "user7"))
        assertNull(scoreService.saveMediaScore(flowId, "GEN4", 5.0f, "user7"))
        assertNull(scoreService.saveMediaScore(flowId, "EMO3", 3.0f, "user7"))

        assertNull(scoreService.saveMediaScore(flowId, "THR1", 4.0f, "user8"))
        assertNull(scoreService.saveMediaScore(flowId, "GEN2", 5.0f, "user8"))

        assertNull(scoreService.saveMediaScore(flowId, "GEN2", 4.5f, "user9"))

        val mediaScoresPagedResult = scoreService.getMediaScorePaged(flowId, page = 0)
        assertInstanceOf<Success<List<MediaScore>>>(mediaScoresPagedResult)
        val mediaScores = mediaScoresPagedResult.value.map { it.toComparableTestMediaScore() }

        assertAll(
            { assertEquals(expectedMediaScoresForFlow_GEN2, mediaScores[0]) },
            { assertEquals(expectedMediaScoresForFlow_THR1, mediaScores[1]) },
            { assertEquals(expectedMediaScoresForFlow_GEN4, mediaScores.find { it.flavourId == "GEN4" }) },
            { assertEquals(expectedMediaScoresForFlow_EMO3, mediaScores.find { it.flavourId == "EMO3" }) },
        )

        assertEquals(4, scoreService.countScoredFlavoursForMediaIfExists(flowId))
        val mediaScorePagedByUserResult = scoreService.getMediaScorePagedByUser("user7", flowId, page = 0)
        assertInstanceOf<Success<List<MediaScore>>>(mediaScorePagedByUserResult)
        val mediaScorePagedByUser = mediaScorePagedByUserResult.value.map { it.toComparableTestMediaScore() }
        val firstTwoMediaScores = mediaScorePagedByUser.take(2)

        assertAll(
            { assertEquals(4, mediaScorePagedByUser.size) },
            { assertContains(firstTwoMediaScores, expectedMediaScoreForFlow_byUser7_GEN2) },
            { assertContains(firstTwoMediaScores, expectedMediaScoreForFlow_byUser7_GEN4) },
            { assertEquals(expectedMediaScoreForFlow_byUser7_THR1, mediaScorePagedByUser[2]) },
            { assertEquals(expectedMediaScoreForFlow_byUser7_EMO3, mediaScorePagedByUser[3]) },
        )

        assertNull(
            scoreService.getMediaScoreByUsernameAndMediaIdAndFlavourId("user8", flowId, "EMO3")
                .succeedOrElse { throw IllegalStateException("`flow` media should exist") }
        )

        val mediaScore = scoreService.getMediaScoreByUsernameAndMediaIdAndFlavourId("user8", flowId, "GEN2")
            .succeedOrElse { throw IllegalStateException("`flow` media should exist") }
        assertNotNull(mediaScore)

        assertAll(
            { assertEquals(5.0f, mediaScore.score) },
            { assertEquals("GEN2", mediaScore.flavour.id) },
            { assertEquals(1, mediaScore.count) },
        )
    }

    @Test
    fun `rate flow - failure`() {
        val flowId = testHelperService.getFlowId()
        val try1 = scoreService.saveMediaScore(Long.MIN_VALUE, "THR1", 4.0f, "user7")
        assertNotNull(try1)
        assertInstanceOf<MediaDoesNotExistError>(try1)

        val try2 = scoreService.saveMediaScore(flowId, "XXX1", 2.0f, "user7")
        assertNotNull(try2)
        assertInstanceOf<FlavourDoesNotExistError>(try2)

        val try3 = scoreService.saveMediaScore(flowId, "THR1", 7.5f, "user7")
        assertNotNull(try3)
        assertInstanceOf<InvalidScoreError>(try3)
    }
}