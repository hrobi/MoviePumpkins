package net.moviepumpkins.core.media.review

import net.moviepumpkins.core.app.config.AuthenticationFacade
import net.moviepumpkins.core.app.config.ReviewingProperties
import net.moviepumpkins.core.app.exception.ConflictException
import net.moviepumpkins.core.app.exception.ForbiddenException
import net.moviepumpkins.core.app.exception.NotFoundException
import net.moviepumpkins.core.app.mapping.toBadRequestException
import net.moviepumpkins.core.integration.controllers.MediaReviewsController
import net.moviepumpkins.core.integration.controllers.ReviewsController
import net.moviepumpkins.core.integration.controllers.ReviewsRateController
import net.moviepumpkins.core.integration.models.GetPagedReviewsResponse
import net.moviepumpkins.core.integration.models.PaginationInfo
import net.moviepumpkins.core.integration.models.ReviewRatingDTO
import net.moviepumpkins.core.integration.models.SaveReviewRequest
import net.moviepumpkins.core.media.review.mapping.toGetReviewResponse
import net.moviepumpkins.core.media.review.mapping.toReviewContent
import net.moviepumpkins.core.media.review.mapping.toReviewRatingType
import net.moviepumpkins.core.media.review.model.DisallowedUserError
import net.moviepumpkins.core.media.review.model.InvalidReviewContentError
import net.moviepumpkins.core.media.review.model.MediaNotFoundError
import net.moviepumpkins.core.media.review.model.Review
import net.moviepumpkins.core.media.review.model.ReviewDoesNotExistError
import net.moviepumpkins.core.media.review.model.UserRatingOwnReviewError
import net.moviepumpkins.core.util.result.succeedOrElse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class ReviewController(
    private val reviewService: ReviewService,
    private val reviewingProperties: ReviewingProperties,
    private val authenticationFacade: AuthenticationFacade,
) : MediaReviewsController, ReviewsController, ReviewsRateController {
    override fun getReviewsByPageOrCreator(
        id: Long,
        page: Int?,
        creator: String?,
    ): ResponseEntity<GetPagedReviewsResponse> {
        if (creator == null) {
            val reviews = reviewService.getPagedReviews(
                id,
                (page ?: 1) - 1,
                if (authenticationFacade.authenticated) authenticationFacade.authenticationName else null
            ).succeedOrElse {
                when (cause) {
                    MediaNotFoundError -> throw NotFoundException()
                }
            }

            val count = reviewService.countReviewsForMediaIfExists(mediaId = id) ?: throw NotFoundException()
            val paginationInfo = PaginationInfo(
                pageCount = reviewingProperties.derivePageCount(count),
                pageSize = reviewingProperties.pageSize
            )

            return ResponseEntity.ok(
                GetPagedReviewsResponse(
                    reviews.map(Review::toGetReviewResponse),
                    paginationInfo
                )
            )
        }

        val review = reviewService.getReview(mediaId = id, creatorUsername = creator)
        if (review == null) {
            throw NotFoundException()
        } else {
            return ResponseEntity.ok(
                GetPagedReviewsResponse(
                    listOf(review.toGetReviewResponse()),
                    PaginationInfo(pageCount = 1, pageSize = reviewingProperties.pageSize)
                )
            )
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

    override fun getOwnReviewRating(id: Long): ResponseEntity<ReviewRatingDTO> {
        val ratingType =
            reviewService.retrieveRatingOrNullIfReviewDoesntExist(id, authenticationFacade.authenticationName)
                ?.toReviewRatingType()
                ?: throw NotFoundException()

        return ResponseEntity.ok(ReviewRatingDTO(ratingType))
    }

    override fun saveReviewRating(reviewRatingDTO: ReviewRatingDTO, id: Long): ResponseEntity<Unit> {
        reviewService.rateReviewOrError(
            id, authenticationFacade.authenticationName, reviewRatingDTO.type.toReviewRatingType()
        )?.let {
            when (it) {
                ReviewDoesNotExistError -> throw NotFoundException()
                UserRatingOwnReviewError -> throw ConflictException()
            }
        }

        return ResponseEntity.noContent().build()
    }
}