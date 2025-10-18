package net.moviepumpkins.core.media.review.mapping

import net.moviepumpkins.core.media.review.entity.ReviewEntity
import net.moviepumpkins.core.media.review.entity.ReviewLikeEntity
import net.moviepumpkins.core.media.review.model.Review
import net.moviepumpkins.core.media.review.model.ReviewRatingType
import net.moviepumpkins.core.user.mapping.toUserAccount
import java.time.ZoneOffset

fun ReviewEntity.toReview() = Review(
    id = id!!,
    title = title,
    content = content,
    spoilerFree = spoilerFree,
    likes = reviewLikesAggregateView?.likes ?: 0,
    dislikes = reviewLikesAggregateView?.dislikes ?: 0,
    createdAt = createdAt.atOffset(ZoneOffset.UTC),
    modifiedAt = modifiedAt.atOffset(ZoneOffset.UTC),
    creator = creator.toUserAccount()
)

fun ReviewLikeEntity?.toReviewRatingType() = this?.let {
    if (it.isLiked) {
        ReviewRatingType.LIKE
    } else {
        ReviewRatingType.DISLIKE
    }
} ?: ReviewRatingType.NO_RATING