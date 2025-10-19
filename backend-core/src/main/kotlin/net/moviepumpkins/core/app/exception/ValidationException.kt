package net.moviepumpkins.core.app.exception

import io.konform.validation.ValidationError
import io.konform.validation.ValidationResult

class ValidationException(val validationErrors: List<ValidationError>) : RuntimeException()

fun <T> ValidationResult<T>.throwIfInvalid() {
    if (!isValid) {
        throw ValidationException(errors)
    }
}