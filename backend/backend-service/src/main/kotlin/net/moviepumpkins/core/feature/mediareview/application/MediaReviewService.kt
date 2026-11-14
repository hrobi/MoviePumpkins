package net.moviepumpkins.core.feature.mediareview.application

import jakarta.transaction.Transactional
import net.moviepumpkins.core.api.models.OwnReviewDto
import net.moviepumpkins.core.api.models.PagedReviewDto
import net.moviepumpkins.core.api.models.PaginationDto
import net.moviepumpkins.core.api.models.ReviewDto
import net.moviepumpkins.core.api.models.ReviewLikeDto
import net.moviepumpkins.core.api.models.UpsertReviewDto
import net.moviepumpkins.core.feature.media.MediaRepository
import net.moviepumpkins.core.feature.media.error.MediaNotFoundException
import net.moviepumpkins.core.feature.mediareview.ReviewRepository
import net.moviepumpkins.core.feature.mediareview.error.LikingOwnReviewForbiddenException
import net.moviepumpkins.core.feature.mediareview.error.MediaReviewAlreadyExists
import net.moviepumpkins.core.feature.mediareview.error.MediaReviewNotFoundException
import net.moviepumpkins.core.feature.mediareview.error.MediaReviewUpdateDeniedException
import net.moviepumpkins.core.feature.mediareview.likes.ReviewLike
import net.moviepumpkins.core.feature.mediareview.likes.ReviewLikeAggregateRepository
import net.moviepumpkins.core.feature.mediareview.likes.ReviewLikeId
import net.moviepumpkins.core.feature.mediareview.likes.ReviewLikeRepository
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
    private val reviewLikeRepository: ReviewLikeRepository,
    private val mediaRepository: MediaRepository,
    private val reviewLikeAggregateRepository: ReviewLikeAggregateRepository,
) {

    @Transactional
    fun getMediaReviews(mediaId: Long, page: Int): PagedReviewDto {
        if (!mediaRepository.existsById(mediaId)) {
            throw MediaNotFoundException(mediaId)
        }

        val reviewPage = reviewRepository.findPageByMediaId(
            mediaId = mediaId,
            pageable = PageRequest.of(page - 1, REVIEW_PAGE_SIZE),
        )
        val likedReviewIds = findLikedReviewIds(reviewPage.content.map { it.id!! })

        return PagedReviewDto(
            paging = PaginationDto(
                pageCount = reviewPage.totalPages,
                pageSize = REVIEW_PAGE_SIZE,
                currentPage = page,
            ),
            items = reviewPage.content.map { review ->
                ReviewMapper.toResponse(review, likedReviewIds?.contains(review.id!!))
            },
        )
    }

    @Transactional
    fun getMediaReview(mediaId: Long, reviewId: Long): ReviewDto {
        if (!mediaRepository.existsById(mediaId)) {
            throw MediaNotFoundException(mediaId)
        }
        val review = reviewRepository.findByMediaIdAndReviewId(
            mediaId,
            reviewId,
        )
            ?: throw MediaReviewNotFoundException(mediaId, reviewId)

        val likedReviewIds = findLikedReviewIds(listOf(reviewId))
        return ReviewMapper.toResponse(review, likedReviewIds?.contains(reviewId))
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
        val review = reviewRepository.findByReviewerUsernameAndMediaId(getAuthUsernameOrThrow(), mediaId)
        val dto = review?.let {
            ReviewMapper.toResponse(it, false)
        }
        return OwnReviewDto(exists = dto != null, data = dto)
    }

    @Transactional
    fun toggleLike(mediaId: Long, reviewId: Long): ReviewLikeDto {
        val currentUsername = getAuthUsernameOrThrow()
        val review = reviewRepository.findByIdWithReviewer(reviewId)
            ?: throw MediaReviewNotFoundException(mediaId, reviewId)
        if (review.reviewer.username == currentUsername) {
            throw LikingOwnReviewForbiddenException()
        }
        val id = ReviewLikeId(reviewId, currentUsername)
        val likeByUserExists = reviewLikeRepository.existsById(id)
        val currentLikeCount = reviewLikeAggregateRepository.findById(reviewId).map { it.likeSum }.orElse(0)
        if (!likeByUserExists) {
            reviewLikeRepository.save(ReviewLike(id))
            return ReviewLikeDto(likedByYou = true, currentLikeCount)
        } else {
            reviewLikeRepository.deleteById(id)
            return ReviewLikeDto(likedByYou = false, currentLikeCount)
        }
    }

    private fun findLikedReviewIds(reviewIds: List<Long>): Set<Long>? {
        val username = getAuthUsernameOrNull() ?: return null
        return reviewLikeRepository.findReviewIdsLikedByUser(reviewIds, username)
    }

}
