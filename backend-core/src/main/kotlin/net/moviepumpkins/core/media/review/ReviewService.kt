package net.moviepumpkins.core.media.review

import jakarta.transaction.Transactional
import net.moviepumpkins.core.app.config.ReviewingProperties
import net.moviepumpkins.core.app.model.UserAccount
import net.moviepumpkins.core.app.model.UserRole
import net.moviepumpkins.core.media.mediadetails.entity.MediaEntity
import net.moviepumpkins.core.media.mediadetails.entity.MediaRepository
import net.moviepumpkins.core.media.review.entity.ReviewEntity
import net.moviepumpkins.core.media.review.entity.ReviewLikeEntity
import net.moviepumpkins.core.media.review.entity.ReviewLikeRepository
import net.moviepumpkins.core.media.review.entity.ReviewRepository
import net.moviepumpkins.core.media.review.mapping.toReview
import net.moviepumpkins.core.media.review.mapping.toReviewRatingType
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
import net.moviepumpkins.core.media.review.model.UserRatingOwnReviewError
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
    private val reviewingProperties: ReviewingProperties,
    private val validateReviewService: ValidateReviewService,
) {
    fun getPagedReviews(
        mediaId: Long,
        page: Int,
        username: String? = null,
    ): Result<List<Review>, ErrorFindingPagedReviews> {
        mediaRepository.existsById(mediaId).trueOrElse { return Failure(MediaNotFoundError) }

        val mediaReference = mediaRepository.getReferenceById(mediaId)
        val reviewsWithoutOwnRating = reviewRepository.findByMedia(
            mediaReference,
            PageRequest.of(page, reviewingProperties.pageSize, Sort.by(MediaEntity::modifiedAt.name).descending())
        ).map(ReviewEntity::toReview)

        if (username != null) {
            val reviewLikes = reviewLikeRepository.findByRaterUsernameAndReviewIdIn(
                username,
                reviewsWithoutOwnRating.map { it.id }
            )

            val reviewLikesByReviewId = reviewLikes.groupBy { it.review.id }
            Success(reviewsWithoutOwnRating.map {
                val reviewLike = reviewLikesByReviewId.get(it.id)
                it.copy(
                    userOwnRating = reviewLike?.get(0).toReviewRatingType()
                )
            })
        }

        return Success(reviewsWithoutOwnRating)
    }

    fun getReview(mediaId: Long, creatorUsername: String): Review? {
        return reviewRepository.findByMediaAndCreator(
            mediaRepository.getReferenceById(mediaId),
            userRepository.getReferenceById(creatorUsername)
        )?.let(ReviewEntity::toReview)
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

    fun countReviewsForMediaIfExists(mediaId: Long): Int? {
        return reviewRepository.countByMediaId(mediaId)
    }

    @Transactional
    fun rateReviewOrError(reviewId: Long, raterUsername: String, ratingType: ReviewRatingType): ErrorRatingReview? {
        val reviewEntity = reviewRepository.findByIdOrNull(reviewId) ?: return ReviewDoesNotExistError

        val userReference = userRepository.getReferenceById(raterUsername)

        val reviewLikeIdIfExists = reviewLikeRepository.getIdByReviewAndRater(reviewEntity, userReference)

        when (ratingType) {
            ReviewRatingType.LIKE, ReviewRatingType.DISLIKE -> {
                if (reviewEntity.creator.username == raterUsername) {
                    return UserRatingOwnReviewError
                }
                reviewLikeRepository.save(
                    ReviewLikeEntity(
                        id = reviewLikeIdIfExists,
                        review = reviewEntity,
                        rater = userReference,
                        isLiked = ratingType == ReviewRatingType.LIKE
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

    @Transactional
    fun retrieveRatingOrNullIfReviewDoesntExist(reviewId: Long, raterUsername: String): ReviewRatingType? {
        if (!reviewRepository.existsById(reviewId)) {
            return null
        }
        return reviewLikeRepository.findByReviewAndRater(
            reviewRepository.getReferenceById(reviewId),
            userRepository.getReferenceById(raterUsername)
        )?.let { if (it.isLiked) ReviewRatingType.LIKE else ReviewRatingType.DISLIKE } ?: ReviewRatingType.NO_RATING
    }
}