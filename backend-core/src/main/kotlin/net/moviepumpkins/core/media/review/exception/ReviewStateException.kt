package net.moviepumpkins.core.media.review.exception

class ReviewStateException(val state: ReviewStateError) : RuntimeException()

sealed interface ReviewStateError {
    data class MediaDoesNotExist(val id: Long) : ReviewStateError
    data class CreatorDoesNotExist(val username: String) : ReviewStateError
    data class ReviewDoesNotExist(val reviewId: Long) : ReviewStateError
    data object ReviewUpdateOrRemovalForbiddenForUser : ReviewStateError
    data object ReviewAlreadyExists : ReviewStateError
}