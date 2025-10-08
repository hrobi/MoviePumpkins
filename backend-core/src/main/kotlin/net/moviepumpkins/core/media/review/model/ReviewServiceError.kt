package net.moviepumpkins.core.media.review.model

import io.konform.validation.ValidationError

sealed interface ErrorFindingPagedReviews
sealed interface ErrorSavingReviewContent
sealed interface ErrorRemovingReview
sealed interface ErrorRatingReview
sealed interface ErrorRevokingReviewRating

data object MediaNotFoundError : ErrorFindingPagedReviews, ErrorSavingReviewContent
data object ReviewDoesNotExistError : ErrorRemovingReview, ErrorRatingReview, ErrorRevokingReviewRating
data object DisallowedUserError : ErrorRemovingReview
data class InvalidReviewContentError(val validationErrors: List<ValidationError>) : ErrorSavingReviewContent