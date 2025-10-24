package net.moviepumpkins.core.media.review.mapping

import net.moviepumpkins.core.integration.models.CreateReviewRequest
import net.moviepumpkins.core.integration.models.UpdateReviewRequest
import net.moviepumpkins.core.media.review.model.ReviewContent

object ReviewContentMapper {
    fun fromCreateReviewRequest(createReviewRequest: CreateReviewRequest): ReviewContent {
        return with(createReviewRequest) {
            ReviewContent(title = title, content = content, spoilerFree = spoilerFree)
        }
    }

    fun fromUpdateReviewRequest(updateReviewRequest: UpdateReviewRequest): ReviewContent {
        return with(updateReviewRequest) {
            ReviewContent(title = title, content = content, spoilerFree = spoilerFree)
        }
    }
}