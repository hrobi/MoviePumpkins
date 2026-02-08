package net.moviepumpkins.core.shared.api.advice

import net.moviepumpkins.core.shared.service.exception.ServiceError
import net.moviepumpkins.core.shared.service.exception.ServiceException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.AuthenticationException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import net.moviepumpkins.core.api.models.ServiceError as ApiServiceError

@ControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(AuthenticationException::class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    fun handleAuthenticationException(authenticationException: AuthenticationException): ApiServiceError {
        return ApiServiceError(
            code = "authenticationRequired",
            message = "This operation requires authentication (Bearer token in authorization header)",
            details = mapOf("originalExceptionMessage" to authenticationException.message)
        )
    }

    @ExceptionHandler(ServiceException::class)
    fun handleApiBoundaryException(serviceException: ServiceException): ResponseEntity<ApiServiceError> {
        return when (val error = serviceException.serviceError) {
            is ServiceError.ResourceNotFound -> apiServiceError(
                statusCode = HttpStatus.NOT_FOUND,
                details = mapOf(error.resourceName to error.resourceValue),
                message = serviceException.message,
                code = serviceException.code
            )

            is ServiceError.SameIdResourceConflict -> apiServiceError(
                statusCode = HttpStatus.CONFLICT,
                details = mapOf(error.idName to error.id),
                message = serviceException.message,
                code = serviceException.code
            )
        }
    }

    private fun apiServiceError(statusCode: HttpStatus, code: String, message: String, details: Map<String, Any>) =
        ResponseEntity
            .status(statusCode)
            .body(
                ApiServiceError(
                    code,
                    message,
                    details
                )
            )

}