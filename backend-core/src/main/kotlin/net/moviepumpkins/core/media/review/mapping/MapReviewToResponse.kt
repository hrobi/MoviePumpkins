package net.moviepumpkins.core.media.review.mapping

import net.moviepumpkins.core.integration.models.GetReviewResponse
import net.moviepumpkins.core.media.review.model.Review
import net.moviepumpkins.core.user.toUserProfile

fun Review.toGetReviewResponse() = GetReviewResponse(
    user = creator.toUserProfile(),
    title = title,
    content = content,
    spoilerFree = spoilerFree,
    createdAt = createdAt,
    modifiedAt = modifiedAt,
    likes = likes,
    dislikes = dislikes
)