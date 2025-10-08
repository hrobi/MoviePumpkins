package net.moviepumpkins.core.media.review

import net.moviepumpkins.core.app.config.AuthenticationFacade
import net.moviepumpkins.core.app.exception.ForbiddenException
import net.moviepumpkins.core.app.exception.NotFoundException
import net.moviepumpkins.core.app.mapping.toBadRequestException
import net.moviepumpkins.core.integration.controllers.MediaReviewsController
import net.moviepumpkins.core.integration.controllers.ReviewsController
import net.moviepumpkins.core.integration.controllers.ReviewsRateController
import net.moviepumpkins.core.integration.models.GetReviewResponse
import net.moviepumpkins.core.integration.models.SaveReviewRatingRequest
import net.moviepumpkins.core.integration.models.SaveReviewRatingRequestType
import net.moviepumpkins.core.integration.models.SaveReviewRequest
import net.moviepumpkins.core.media.review.mapping.toGetReviewResponse
import net.moviepumpkins.core.media.review.mapping.toReviewContent
import net.moviepumpkins.core.media.review.model.DisallowedUserError
import net.moviepumpkins.core.media.review.model.InvalidReviewContentError
import net.moviepumpkins.core.media.review.model.MediaNotFoundError
import net.moviepumpkins.core.media.review.model.Review
import net.moviepumpkins.core.media.review.model.ReviewDoesNotExistError
import net.moviepumpkins.core.media.review.model.ReviewRatingType
import net.moviepumpkins.core.util.result.succeedOrElse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class ReviewController(
    private val reviewService: ReviewService,
    private val authenticationFacade: AuthenticationFacade,
) : MediaReviewsController, ReviewsController, ReviewsRateController {
    override fun getReviewsByPageOrCreator(
        id: Long,
        page: Int?,
        creator: String?,
    ): ResponseEntity<List<GetReviewResponse>> {
        if (creator == null) {
            val reviews = reviewService.getPagedReviews(id, page ?: 1).succeedOrElse {
                when (cause) {
                    MediaNotFoundError -> throw NotFoundException()
                }
            }
            return ResponseEntity.ok(reviews.map(Review::toGetReviewResponse))
        }

        val review = reviewService.getReview(mediaId = id, creatorUsername = creator)
        if (review == null) {
            throw NotFoundException()
        } else {
            return ResponseEntity.ok(listOf(review.toGetReviewResponse()))
        }
    }

    override fun saveReview(saveReviewRequest: SaveReviewRequest, id: Long): ResponseEntity<Unit> {
        reviewService.saveReviewOrError(
            mediaId = id,
            creatorUsername = authenticationFacade.authenticationName,
            reviewContent = saveReviewRequest.toReviewContent()
        )?.let {
            when (it) {
                is InvalidReviewContentError -> throw it.validationErrors.toBadRequestException()
                MediaNotFoundError -> throw NotFoundException()
            }
        }

        return ResponseEntity.ok(Unit)
    }

    override fun deleteReview(id: Long): ResponseEntity<Unit> {
        reviewService.removeReviewOrError(id, remover = authenticationFacade.extractUserAccount())?.let {
            when (it) {
                ReviewDoesNotExistError -> throw NotFoundException()
                DisallowedUserError -> throw ForbiddenException()
            }
        }
        return ResponseEntity.noContent().build()
    }

    override fun saveReviewRating(saveReviewRatingRequest: SaveReviewRatingRequest, id: Long): ResponseEntity<Unit> {
        reviewService.rateReviewOrError(
            id, authenticationFacade.authenticationName, when (saveReviewRatingRequest.type) {
                SaveReviewRatingRequestType.LIKE -> ReviewRatingType.LIKE
                SaveReviewRatingRequestType.DISLIKE -> ReviewRatingType.DISLIKE
                SaveReviewRatingRequestType.NO_RATING -> ReviewRatingType.NO_RATING
            }
        )?.let {
            when (it) {
                ReviewDoesNotExistError -> throw NotFoundException()
            }
        }

        return ResponseEntity.noContent().build()
    }
}