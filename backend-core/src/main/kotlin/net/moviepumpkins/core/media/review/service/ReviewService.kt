package net.moviepumpkins.core.media.review.service

import jakarta.transaction.Transactional
import net.moviepumpkins.core.app.exception.throwIfInvalid
import net.moviepumpkins.core.app.model.UserAccount
import net.moviepumpkins.core.app.model.UserRole
import net.moviepumpkins.core.media.mediadetails.entity.MediaRepository
import net.moviepumpkins.core.media.review.entity.ReviewEntity
import net.moviepumpkins.core.media.review.entity.ReviewRepository
import net.moviepumpkins.core.media.review.exception.ReviewStateError
import net.moviepumpkins.core.media.review.exception.ReviewStateException
import net.moviepumpkins.core.media.review.mapping.toReview
import net.moviepumpkins.core.media.review.model.Review
import net.moviepumpkins.core.media.review.model.ReviewContent
import net.moviepumpkins.core.media.review.validation.ValidateReviewService
import net.moviepumpkins.core.user.repository.UserAccountRepository
import net.moviepumpkins.core.util.getLogger
import net.moviepumpkins.core.util.jpa.getReferenceByIdOrThrow
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Component

private const val PAGE_SIZE = 5

@Component
class ReviewService(
    private val mediaRepository: MediaRepository,
    private val reviewRepository: ReviewRepository,
    private val userRepository: UserAccountRepository,
    private val validateReviewService: ValidateReviewService,
) {
    fun getPagedReviews(
        mediaId: Long,
        page: Int,
    ): List<Review> {

        val mediaReference = mediaRepository.getReferenceByIdOrThrow(mediaId) {
            ReviewStateException(
                ReviewStateError.MediaDoesNotExist(mediaId)
            )
        }

        val reviews = reviewRepository.findByMedia(
            mediaReference,
            PageRequest.of(page, PAGE_SIZE, Sort.by(ReviewEntity::modifiedAt.name).descending())
        ).map(ReviewEntity::toReview)

        return reviews
    }

    fun getReview(mediaId: Long, creatorUsername: String): Review? {
        return reviewRepository.findByMediaAndCreator(
            mediaRepository.getReferenceById(mediaId),
            userRepository.getReferenceById(creatorUsername)
        )?.let(ReviewEntity::toReview)
    }

    @Transactional
    fun createReview(
        mediaId: Long,
        creatorUsername: String,
        reviewContent: ReviewContent,
    ): Long {
        validateReviewService.validateReviewContent(reviewContent).throwIfInvalid()

        val mediaReference = mediaRepository.getReferenceByIdOrThrow(mediaId) {
            ReviewStateException(
                ReviewStateError.MediaDoesNotExist(mediaId)
            )
        }
        val creatorReference = userRepository.getReferenceByIdOrThrow(creatorUsername) {
            ReviewStateException(
                ReviewStateError.CreatorDoesNotExist(creatorUsername)
            )
        }

        val id = reviewRepository.getIdByMediaAndCreatorOrNull(mediaReference, creatorReference)

        if (id != null) {
            throw ReviewStateException(ReviewStateError.ReviewAlreadyExists)
        }

        val reviewEntity = reviewRepository.save(
            ReviewEntity(
                creator = creatorReference,
                media = mediaReference,
                title = reviewContent.title,
                content = reviewContent.content,
                spoilerFree = reviewContent.spoilerFree,
            )
        )

        reviewRepository.flush()

        return reviewEntity.id!!
    }

    @Transactional
    fun updateReview(reviewId: Long, updaterUsername: String, updateContent: ReviewContent) {
        val reviewEntity = reviewRepository.findById(reviewId)
            .orElseThrow { ReviewStateException(ReviewStateError.ReviewAlreadyExists) }

        getLogger().debug("updaterUsername=$updaterUsername")
        getLogger().debug("creatorUsername=${reviewEntity.creator.username}")

        if (updaterUsername != reviewEntity.creator.username) {
            throw ReviewStateException(ReviewStateError.ReviewUpdateOrRemovalForbiddenForUser)
        }

        validateReviewService.validateReviewContent(updateContent).throwIfInvalid()

        with(reviewEntity) {
            title = updateContent.title
            content = updateContent.content
            spoilerFree = updateContent.spoilerFree
        }
    }

    @Transactional
    fun removeReview(id: Long, remover: UserAccount) {
        val reviewEntity = reviewRepository.findByIdOrNull(id)
            ?: throw ReviewStateException(ReviewStateError.ReviewDoesNotExist(id))
        if (reviewEntity.creator.username != remover.username && !remover.role.isAtLeast(UserRole.SUPERVISOR)) {
            throw ReviewStateException(ReviewStateError.ReviewUpdateOrRemovalForbiddenForUser)
        }
        reviewRepository.delete(reviewEntity)
    }

    fun countReviewsForMediaIfExists(mediaId: Long): Int {
        return reviewRepository.countByMediaId(mediaId)
    }
}