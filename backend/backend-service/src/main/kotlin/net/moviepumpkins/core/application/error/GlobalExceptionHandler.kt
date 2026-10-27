package net.moviepumpkins.core.application.error

import net.moviepumpkins.core.api.models.ApiServiceErrorDto
import net.moviepumpkins.core.api.models.ConstraintViolationDto
import net.moviepumpkins.core.api.models.ValidationErrorDto
import net.moviepumpkins.core.api.models.ValidationFailureDto
import net.moviepumpkins.core.api.models.ValidationFailureDtoDetails
import net.moviepumpkins.core.shared.application.BusinessExceptionMessageResolver
import net.moviepumpkins.core.shared.application.ValidationErrorMessageResolver
import net.moviepumpkins.core.shared.error.BusinessException
import net.moviepumpkins.core.shared.error.ValidationException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.AuthenticationException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus

@ControllerAdvice
class GlobalExceptionHandler(
    private val businessExceptionMessageResolver: BusinessExceptionMessageResolver,
    private val validationErrorMessageResolver: ValidationErrorMessageResolver
) {

    @ExceptionHandler(AuthenticationException::class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    fun handleAuthenticationException(authenticationException: AuthenticationException): ApiServiceErrorDto {
        return ApiServiceErrorDto(
            code = "authenticationRequired",
            message = "This operation requires authentication (Bearer token in authorization header)",
            details = mapOf("originalExceptionMessage" to authenticationException.message)
        )
    }

    @ExceptionHandler(BusinessException::class)
    fun handleBusinessException(businessException: BusinessException): ResponseEntity<ApiServiceErrorDto> {
        val response = ApiServiceErrorDto(
            code = businessException.javaClass.name.removeSuffix("Exception"),
            message = businessExceptionMessageResolver.resolveOrNull(businessException) ?: "unknown error"
        )
        return ResponseEntity.status(businessException.status).body(response)
    }

    @ExceptionHandler(ValidationException::class)
    fun handleBusinessException(validationException: ValidationException): ResponseEntity<ValidationFailureDto> {
        val errors = validationException.validationErrors.map {
            ValidationErrorDto(
                path = it.path ?: "",
                violations = it.violations.map {
                    ConstraintViolationDto(
                        it.javaClass.name.removeSuffix("Constraint"),
                        validationErrorMessageResolver.resolveOrNull(it) ?: "unknown error"
                    )
                }
            )
        }
        val response = ValidationFailureDto(
            code = validationException.javaClass.name,
            message = businessExceptionMessageResolver.resolveOrNull(validationException) ?: "unknown error",
            details = ValidationFailureDtoDetails(errors)
        )
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response)
    }

}