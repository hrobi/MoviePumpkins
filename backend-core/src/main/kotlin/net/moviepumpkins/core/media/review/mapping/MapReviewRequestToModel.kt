package net.moviepumpkins.core.media.review.mapping

import net.moviepumpkins.core.integration.models.SaveReviewRequest
import net.moviepumpkins.core.media.review.model.ReviewContent

fun SaveReviewRequest.toReviewContent() = ReviewContent(title = title, content = content, spoilerFree = spoilerFree)