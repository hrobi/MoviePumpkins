package net.moviepumpkins.core.media.review

import net.moviepumpkins.core.app.config.AuthenticationFacade
import net.moviepumpkins.core.app.exception.NotFoundException
import net.moviepumpkins.core.integration.controllers.MediaReviewsController
import net.moviepumpkins.core.integration.controllers.ReviewsController
import net.moviepumpkins.core.integration.models.CreateReviewRequest
import net.moviepumpkins.core.integration.models.GetReviewResponse
import net.moviepumpkins.core.integration.models.GetReviewsPagedResponse
import net.moviepumpkins.core.integration.models.PaginationInfo
import net.moviepumpkins.core.integration.models.UpdateReviewRequest
import net.moviepumpkins.core.media.review.mapping.ReviewContentMapper
import net.moviepumpkins.core.media.review.model.Review
import net.moviepumpkins.core.media.review.service.ReviewService
import net.moviepumpkins.core.user.toUserProfile
import net.moviepumpkins.core.util.getLogger
import net.moviepumpkins.core.util.getPageCount
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import java.net.URI

private const val PAGE_SIZE = 5

@RestController
class ReviewController(
    private val reviewService: ReviewService,
    private val authenticationFacade: AuthenticationFacade,
) : ReviewsController, MediaReviewsController {

    private fun Review.toGetReviewResponse() = GetReviewResponse(
        title = title,
        content = content,
        spoilerFree = spoilerFree,
        createdAt = createdAt,
        modifiedAt = modifiedAt,
        id = id,
        user = creator.toUserProfile()
    )

    override fun getReviews(
        id: Long,
        reviewerUsername: String?,
        page: Int?,
    ): ResponseEntity<GetReviewsPagedResponse> {

        if (reviewerUsername != null) {

            getLogger().debug("reviewerUsername=${reviewerUsername}")

            val review =
                reviewService.getReview(id, reviewerUsername) ?: throw NotFoundException()
            return ResponseEntity.ok(
                GetReviewsPagedResponse(
                    reviews = listOf(review.toGetReviewResponse()),
                    pagination = PaginationInfo(
                        pageCount = 1,
                        pageSize = 1
                    )
                )
            )
        }

        val reviews = reviewService.getPagedReviews(id, (page ?: 1) - 1)
        return ResponseEntity.ok(
            GetReviewsPagedResponse(
                reviews = reviews.map { it.toGetReviewResponse() },
                pagination = PaginationInfo(
                    pageCount = getPageCount(reviewService.countReviewsForMediaIfExists(id), PAGE_SIZE),
                    pageSize = PAGE_SIZE
                )
            )
        )
    }

    override fun createReview(createReviewRequest: CreateReviewRequest, id: Long): ResponseEntity<Unit> {
        val reviewId = reviewService.createReview(
            id,
            authenticationFacade.authenticationName,
            ReviewContentMapper.fromCreateReviewRequest(createReviewRequest)
        )

        return ResponseEntity.created(URI.create("/reviews/${reviewId}")).build()
    }

    override fun updateReview(updateReviewRequest: UpdateReviewRequest, id: Long): ResponseEntity<Unit> {
        reviewService.updateReview(
            updateReviewRequest.id,
            authenticationFacade.authenticationName,
            ReviewContentMapper.fromUpdateReviewRequest(updateReviewRequest)
        )

        return ResponseEntity.ok().build()
    }


}