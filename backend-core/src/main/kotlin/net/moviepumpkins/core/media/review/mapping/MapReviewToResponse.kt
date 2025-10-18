package net.moviepumpkins.core.media.review.mapping

import net.moviepumpkins.core.integration.models.GetReviewResponse
import net.moviepumpkins.core.media.review.model.Review
import net.moviepumpkins.core.media.review.model.ReviewRatingType
import net.moviepumpkins.core.user.toUserProfile
import net.moviepumpkins.core.integration.models.ReviewRatingType as ReviewRatingResponseType

fun Review.toGetReviewResponse() = GetReviewResponse(
    id = id,
    user = creator.toUserProfile(),
    title = title,
    content = content,
    spoilerFree = spoilerFree,
    createdAt = createdAt,
    modifiedAt = modifiedAt,
    likes = likes,
    dislikes = dislikes,
    usersOwnRating = userOwnRating?.toReviewRatingType()
)

fun ReviewRatingType.toReviewRatingType() = when (this) {
    ReviewRatingType.LIKE -> ReviewRatingResponseType.LIKE
    ReviewRatingType.DISLIKE -> ReviewRatingResponseType.DISLIKE
    ReviewRatingType.NO_RATING -> ReviewRatingResponseType.NO_RATING
}