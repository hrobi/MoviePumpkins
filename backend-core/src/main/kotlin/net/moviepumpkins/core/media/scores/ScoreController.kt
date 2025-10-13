package net.moviepumpkins.core.media.scores

import net.moviepumpkins.core.app.PagingInfoService
import net.moviepumpkins.core.app.config.AuthenticationFacade
import net.moviepumpkins.core.app.exception.NotFoundException
import net.moviepumpkins.core.app.exception.buildBadRequestException
import net.moviepumpkins.core.integration.controllers.MediaScoresController
import net.moviepumpkins.core.integration.controllers.MediaScoresPagingController
import net.moviepumpkins.core.integration.models.GetMediaScoreResponse
import net.moviepumpkins.core.integration.models.PagingInfo
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
    private val authenticationFacade: AuthenticationFacade,
    private val pagingInfoService: PagingInfoService,
) : MediaScoresController, MediaScoresPagingController {
    override fun getScoresByPage(
        id: Long,
        page: Int?,
        username: String?,
        flavour: String?,
    ): ResponseEntity<List<GetMediaScoreResponse>> {
        if (username != null && flavour != null) {
            val mediaScore = scoreService.getMediaScoreByUsernameAndMediaIdAndFlavourId(username, id, flavour)
                .succeedOrElse { throw NotFoundException() }

            return ResponseEntity.ok(
                mediaScore?.let { listOf(mediaScore.toGetMediaScoreResponse()) } ?: emptyList()
            )
        }

        if (username != null) {
            val mediaScoresPaged = scoreService.getMediaScorePagedByUser(username, id, page = (page ?: 1) - 1)
                .succeedOrElse { throw NotFoundException() }

            return ResponseEntity.ok(mediaScoresPaged.map { it.toGetMediaScoreResponse() })
        }

        val mediaScoresPaged = scoreService.getMediaScorePaged(id, page = (page ?: 1) - 1)
            .succeedOrElse { throw NotFoundException() }

        return ResponseEntity.ok(
            mediaScoresPaged.map { it.toGetMediaScoreResponse() }
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


    override fun getMediaScorePagingInfo(id: Long, username: String?): ResponseEntity<PagingInfo> {
        if (username == null) {
            val count = scoreService.countScoredFlavoursForMediaIfExists(id) ?: throw NotFoundException()
            return ResponseEntity.ok(pagingInfoService.derivePagingInfo(count))
        }

        val count = scoreService.countScoredFlavoursForMediaAndUserIfExists(id, username) ?: throw NotFoundException()
        return ResponseEntity.ok(pagingInfoService.derivePagingInfo(count))
    }


}