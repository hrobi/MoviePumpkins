package net.moviepumpkins.core.shared.error

import net.moviepumpkins.core.shared.validation.ValidationError
import org.springframework.http.HttpStatus

sealed class BusinessException(val status: HttpStatus) : RuntimeException()
abstract class ResourceNotFoundException : BusinessException(HttpStatus.NOT_FOUND)
abstract class ResourceConflictException : BusinessException(HttpStatus.CONFLICT)
abstract class AuthorizationException : BusinessException(HttpStatus.FORBIDDEN)
class ValidationException(val validationErrors: List<ValidationError>) : BusinessException(HttpStatus.BAD_REQUEST)