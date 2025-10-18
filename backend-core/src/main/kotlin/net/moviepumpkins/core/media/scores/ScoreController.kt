package net.moviepumpkins.core.media.scores

import net.moviepumpkins.core.app.config.AuthenticationFacade
import net.moviepumpkins.core.app.config.ScoringProperties
import net.moviepumpkins.core.app.exception.NotFoundException
import net.moviepumpkins.core.app.exception.buildBadRequestException
import net.moviepumpkins.core.integration.controllers.MediaScoresController
import net.moviepumpkins.core.integration.models.GetPagedMediaScoresResponse
import net.moviepumpkins.core.integration.models.PaginationInfo
import net.moviepumpkins.core.integration.models.SaveMediaScoreRequest
import net.moviepumpkins.core.media.scores.mapping.toGetMediaScoreResponse
import net.moviepumpkins.core.media.scores.model.FlavourDoesNotExistError
import net.moviepumpkins.core.media.scores.model.InvalidScoreError
import net.moviepumpkins.core.media.scores.model.MediaDoesNotExistError
import net.moviepumpkins.core.util.result.succeedOrElse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class ScoreController(
    private val scoreService: ScoreService,
    private val scoringProperties: ScoringProperties,
    private val authenticationFacade: AuthenticationFacade,
) : MediaScoresController {
    override fun getScoresByPage(
        id: Long,
        page: Int?,
        username: String?,
        flavour: String?,
    ): ResponseEntity<GetPagedMediaScoresResponse> {
        if (username != null && flavour != null) {
            val mediaScore = scoreService.getMediaScoreByUsernameAndMediaIdAndFlavourId(username, id, flavour)
                .succeedOrElse { throw NotFoundException() }

            return ResponseEntity.ok(
                GetPagedMediaScoresResponse(
                    mediaScore?.let { listOf(mediaScore.toGetMediaScoreResponse()) } ?: emptyList(),
                    PaginationInfo(1, scoringProperties.pageSize)
                )
            )
        }

        if (username != null) {
            val mediaScoresPaged = scoreService.getMediaScorePagedByUser(username, id, page = (page ?: 1) - 1)
                .succeedOrElse { throw NotFoundException() }
            val count = scoreService.countScoredFlavoursForMediaAndUserIfExists(id, username)
            return ResponseEntity.ok(
                GetPagedMediaScoresResponse(
                    mediaScoresPaged.map { it.toGetMediaScoreResponse() },
                    PaginationInfo(scoringProperties.derivePageCount(count ?: 0), scoringProperties.pageSize)
                )
            )
        }

        val mediaScoresPaged = scoreService.getMediaScorePaged(id, page = (page ?: 1) - 1)
            .succeedOrElse { throw NotFoundException() }

        val count = scoreService.countScoredFlavoursForMediaIfExists(mediaId = id)

        return ResponseEntity.ok(
            GetPagedMediaScoresResponse(
                mediaScoresPaged.map { it.toGetMediaScoreResponse() },
                PaginationInfo(scoringProperties.derivePageCount(count ?: 0), scoringProperties.pageSize)
            )
        )
    }

    override fun saveScore(saveMediaScoreRequest: SaveMediaScoreRequest, id: Long): ResponseEntity<Unit> {
        val error = scoreService.saveMediaScoreOrError(
            mediaId = id,
            flavourId = saveMediaScoreRequest.flavourId,
            score = saveMediaScoreRequest.score,
            scoreGiverUsername = authenticationFacade.authenticationName
        )

        when (error) {
            FlavourDoesNotExistError, MediaDoesNotExistError -> throw NotFoundException()
            is InvalidScoreError -> throw buildBadRequestException {
                forField("score", "Score must be between ${error.min} and ${error.max}")
            }

            null -> return ResponseEntity.ok(Unit)
        }
    }


}