package net.moviepumpkins.core.app.advice

import net.moviepumpkins.core.app.exception.ClientErrorException
import net.moviepumpkins.core.app.exception.ValidationException
import net.moviepumpkins.core.integration.models.BadRequestBodyError
import net.moviepumpkins.core.integration.models.BadRequestResponse
import net.moviepumpkins.core.media.review.exception.ReviewStateError
import net.moviepumpkins.core.media.review.exception.ReviewStateException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(ClientErrorException::class)
    fun handleClientErrorException(clientErrorException: ClientErrorException): ResponseEntity<Any> {
        return ResponseEntity.status(clientErrorException.status).body(clientErrorException.body)
    }

    private fun statusOnlyResponse(status: HttpStatus): ResponseEntity<Any> {
        return ResponseEntity.status(status).build()
    }

    @ExceptionHandler(ReviewStateException::class)
    fun handleReviewStateException(reviewStateException: ReviewStateException): ResponseEntity<Any> {
        return when (reviewStateException.state) {
            is ReviewStateError.CreatorDoesNotExist,
            is ReviewStateError.MediaDoesNotExist,
            is ReviewStateError.ReviewDoesNotExist,
            -> statusOnlyResponse(HttpStatus.NOT_FOUND)

            ReviewStateError.ReviewAlreadyExists -> statusOnlyResponse(HttpStatus.CONFLICT)
            ReviewStateError.ReviewUpdateOrRemovalForbiddenForUser -> statusOnlyResponse(HttpStatus.FORBIDDEN)
        }
    }

    @ExceptionHandler(ValidationException::class)
    fun handleValidationException(validationException: ValidationException): ResponseEntity<BadRequestResponse> {
        return ResponseEntity.status(400).body(BadRequestResponse(validationException.validationErrors.map {
            BadRequestBodyError(
                field = it.dataPath.substring(1),
                reason = it.message
            )
        }))
    }
}