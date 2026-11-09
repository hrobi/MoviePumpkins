package net.moviepumpkins.core.feature.mediareview.application

import jakarta.transaction.Transactional
import net.moviepumpkins.core.api.models.OwnReviewDto
import net.moviepumpkins.core.api.models.PagedReviewDto
import net.moviepumpkins.core.api.models.PaginationDto
import net.moviepumpkins.core.api.models.ReviewDto
import net.moviepumpkins.core.api.models.UpsertReviewDto
import net.moviepumpkins.core.feature.media.MediaRepository
import net.moviepumpkins.core.feature.media.error.MediaNotFoundException
import net.moviepumpkins.core.feature.mediareview.ReviewRepository
import net.moviepumpkins.core.feature.mediareview.error.MediaReviewAlreadyExists
import net.moviepumpkins.core.feature.mediareview.error.MediaReviewNotFoundException
import net.moviepumpkins.core.feature.mediareview.error.MediaReviewUpdateDeniedException
import net.moviepumpkins.core.feature.user.UserAccountRepository
import net.moviepumpkins.core.shared.auth.getAuthUsernameOrNull
import net.moviepumpkins.core.shared.auth.getAuthUsernameOrThrow
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Component

private const val REVIEW_PAGE_SIZE = 5

@Component
class MediaReviewService(
    private val userAccountRepository: UserAccountRepository,
    private val reviewRepository: ReviewRepository,
    private val mediaRepository: MediaRepository,
) {

    @Transactional
    fun getMediaReviews(mediaId: Long, page: Int): PagedReviewDto {
        if (!mediaRepository.existsById(mediaId)) {
            throw MediaNotFoundException(mediaId)
        }

        val reviewPage = reviewRepository.findViewPageByMediaId(
            mediaId = mediaId,
            currentUsername = getAuthUsernameOrNull(),
            pageable = PageRequest.of(page - 1, REVIEW_PAGE_SIZE),
        )

        return PagedReviewDto(
            paging = PaginationDto(
                pageCount = reviewPage.totalPages,
                pageSize = REVIEW_PAGE_SIZE,
                currentPage = page,
            ),
            items = reviewPage.content.map(ReviewMapper::toResponse),
        )
    }

    @Transactional
    fun getMediaReview(mediaId: Long, reviewId: Long): ReviewDto {
        if (!mediaRepository.existsById(mediaId)) {
            throw MediaNotFoundException(mediaId)
        }
        val review = reviewRepository.findViewByMediaIdAndReviewId(
            mediaId,
            reviewId,
            getAuthUsernameOrNull(),
        )
            ?: throw MediaReviewNotFoundException(mediaId, reviewId)

        return ReviewMapper.toResponse(review)
    }

    @Transactional
    fun createMediaReviewAndGetId(mediaId: Long, request: UpsertReviewDto): Long {
        val reviewer = userAccountRepository.getReferenceById(getAuthUsernameOrThrow())
        if (!mediaRepository.existsById(mediaId)) {
            throw MediaNotFoundException(mediaId)
        }
        val media = mediaRepository.getReferenceById(mediaId)
        if (reviewRepository.existsByReviewerAndMedia(reviewer, media)) {
            throw MediaReviewAlreadyExists()
        }
        val review = reviewRepository.save(ReviewMapper.fromRequest(request, reviewer, media))
        return review.id!!
    }

    @Transactional
    fun updateMediaReview(mediaId: Long, reviewId: Long, request: UpsertReviewDto) {
        val currentUsername = getAuthUsernameOrThrow()
        if (!mediaRepository.existsById(mediaId)) {
            throw MediaNotFoundException(mediaId)
        }
        val review = reviewRepository.findByMediaIdAndReviewIdWithReviewer(mediaId, reviewId)
            ?: throw MediaReviewNotFoundException(reviewId, mediaId)
        if (currentUsername != review.reviewer.username) {
            throw MediaReviewUpdateDeniedException()
        }
        ReviewMapper.updateByRequest(review, request)
    }

    @Transactional
    fun getOwnMediaReview(mediaId: Long): OwnReviewDto {
        if (!mediaRepository.existsById(mediaId)) {
            throw MediaNotFoundException(mediaId)
        }
        val dto = reviewRepository
            .findViewByReviewerUsernameAndMediaId(getAuthUsernameOrThrow(), mediaId)
            ?.let { ReviewMapper.toResponse(it) }
        return OwnReviewDto(exists = dto != null, data = dto)
    }

}
