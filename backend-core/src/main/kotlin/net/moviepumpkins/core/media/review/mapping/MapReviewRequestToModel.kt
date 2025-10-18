package net.moviepumpkins.core.media.review.mapping

import net.moviepumpkins.core.integration.models.ReviewRatingType
import net.moviepumpkins.core.integration.models.SaveReviewRequest
import net.moviepumpkins.core.media.review.model.ReviewContent
import net.moviepumpkins.core.media.review.model.ReviewRatingType as ReviewRatingTypeModel

fun SaveReviewRequest.toReviewContent() = ReviewContent(title = title, content = content, spoilerFree = spoilerFree)

fun ReviewRatingType.toReviewRatingType() = when (this) {
    ReviewRatingType.LIKE -> ReviewRatingTypeModel.LIKE
    ReviewRatingType.DISLIKE -> ReviewRatingTypeModel.DISLIKE
    ReviewRatingType.NO_RATING -> ReviewRatingTypeModel.NO_RATING
}