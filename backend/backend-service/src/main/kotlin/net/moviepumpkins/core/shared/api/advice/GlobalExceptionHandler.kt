package net.moviepumpkins.core.shared.api.advice

import net.moviepumpkins.core.api.models.ApiServiceErrorDto
import net.moviepumpkins.core.shared.service.exception.ServiceException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.AuthenticationException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus

@ControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(AuthenticationException::class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    fun handleAuthenticationException(authenticationException: AuthenticationException): ApiServiceErrorDto {
        return ApiServiceErrorDto(
            code = "authenticationRequired",
            message = "This operation requires authentication (Bearer token in authorization header)",
            details = mapOf("originalExceptionMessage" to authenticationException.message)
        )
    }

    @ExceptionHandler(ServiceException::class)
    fun handleApiBoundaryException(serviceException: ServiceException): ResponseEntity<ApiServiceErrorDto> {
        return with(serviceException) {
            ResponseEntity.status(status).body(ApiServiceErrorDto(code, message, details))
        }
    }

}