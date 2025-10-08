package net.moviepumpkins.core.media.review

import jakarta.transaction.Transactional
import net.moviepumpkins.core.app.config.ReviewsProperties
import net.moviepumpkins.core.app.model.UserAccount
import net.moviepumpkins.core.app.model.UserRole
import net.moviepumpkins.core.media.mediadetails.entity.MediaEntity
import net.moviepumpkins.core.media.mediadetails.entity.MediaRepository
import net.moviepumpkins.core.media.review.entity.ReviewEntity
import net.moviepumpkins.core.media.review.entity.ReviewLikeEntity
import net.moviepumpkins.core.media.review.entity.ReviewLikeRepository
import net.moviepumpkins.core.media.review.entity.ReviewRepository
import net.moviepumpkins.core.media.review.mapping.toReview
import net.moviepumpkins.core.media.review.model.DisallowedUserError
import net.moviepumpkins.core.media.review.model.ErrorFindingPagedReviews
import net.moviepumpkins.core.media.review.model.ErrorRatingReview
import net.moviepumpkins.core.media.review.model.ErrorRemovingReview
import net.moviepumpkins.core.media.review.model.ErrorSavingReviewContent
import net.moviepumpkins.core.media.review.model.InvalidReviewContentError
import net.moviepumpkins.core.media.review.model.MediaNotFoundError
import net.moviepumpkins.core.media.review.model.Review
import net.moviepumpkins.core.media.review.model.ReviewContent
import net.moviepumpkins.core.media.review.model.ReviewDoesNotExistError
import net.moviepumpkins.core.media.review.model.ReviewRatingType
import net.moviepumpkins.core.media.review.validation.ValidateReviewService
import net.moviepumpkins.core.user.repository.UserAccountRepository
import net.moviepumpkins.core.util.result.Failure
import net.moviepumpkins.core.util.result.Result
import net.moviepumpkins.core.util.result.Success
import net.moviepumpkins.core.util.shortcircuit.emptyOrElse
import net.moviepumpkins.core.util.shortcircuit.trueOrElse
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Component

@Component
class ReviewService(
    private val mediaRepository: MediaRepository,
    private val reviewRepository: ReviewRepository,
    private val reviewLikeRepository: ReviewLikeRepository,
    private val userRepository: UserAccountRepository,
    private val reviewsProperties: ReviewsProperties,
    private val validateReviewService: ValidateReviewService,
) {
    fun getPagedReviews(mediaId: Long, page: Int): Result<List<Review>, ErrorFindingPagedReviews> {
        val mediaReference = mediaRepository.getReferenceById(mediaId)
        mediaReference.id ?: return Failure(MediaNotFoundError)
        val reviews = reviewRepository.findByMedia(
            mediaReference,
            PageRequest.of(page, reviewsProperties.pageSize, Sort.by(MediaEntity::modifiedAt.name).descending())
        ).map(ReviewEntity::toReview)

        return Success(reviews)
    }

    fun getReview(mediaId: Long, creatorUsername: String): Review? {
        return reviewRepository.findByMediaAndCreator(
            mediaRepository.getReferenceById(mediaId),
            userRepository.getReferenceById(creatorUsername)
        )?.let(ReviewEntity::toReview);
    }

    @Transactional
    fun saveReviewOrError(
        mediaId: Long,
        creatorUsername: String,
        reviewContent: ReviewContent,
    ): ErrorSavingReviewContent? {
        validateReviewService.validateReviewContent(reviewContent)
            .emptyOrElse { return InvalidReviewContentError(this) }
        mediaRepository.existsById(mediaId).trueOrElse { return MediaNotFoundError }

        val mediaReference = mediaRepository.getReferenceById(mediaId)
        val creatorReference = userRepository.getReferenceById(creatorUsername)

        reviewRepository.save(
            ReviewEntity(
                id = reviewRepository.getIdByMediaAndCreatorOrNull(mediaReference, creatorReference),
                creator = creatorReference,
                media = mediaReference,
                title = reviewContent.title,
                content = reviewContent.content,
                spoilerFree = reviewContent.spoilerFree,
            )
        )

        return null
    }

    @Transactional
    fun removeReviewOrError(id: Long, remover: UserAccount): ErrorRemovingReview? {
        val reviewEntity = reviewRepository.findByIdOrNull(id) ?: return ReviewDoesNotExistError
        if (reviewEntity.creator.username != remover.username || remover.role.isAtLeast(UserRole.SUPERVISOR)) {
            return DisallowedUserError
        }
        reviewRepository.delete(reviewEntity)
        return null
    }

    @Transactional
    fun rateReviewOrError(reviewId: Long, raterUsername: String, ratingType: ReviewRatingType): ErrorRatingReview? {
        if (!reviewRepository.existsById(reviewId)) {
            return ReviewDoesNotExistError
        }

        val reviewReference = reviewRepository.getReferenceById(reviewId)
        val userReference = userRepository.getReferenceById(raterUsername)

        val reviewLikeIdIfExists = reviewLikeRepository.getIdByReviewAndRater(reviewReference, userReference)

        when (ratingType) {
            ReviewRatingType.LIKE, ReviewRatingType.DISLIKE -> {
                reviewLikeRepository.save(
                    ReviewLikeEntity(
                        id = reviewLikeIdIfExists,
                        review = reviewReference,
                        rater = userReference,
                        isLike = ratingType == ReviewRatingType.LIKE
                    )
                )
            }

            ReviewRatingType.NO_RATING -> {
                val reviewLikeId = reviewLikeIdIfExists ?: return null
                reviewLikeRepository.deleteById(reviewLikeId)
            }
        }

        return null
    }
}