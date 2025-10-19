package net.moviepumpkins.core.app.validation

import io.konform.validation.Validation
import io.konform.validation.constraints.maxLength
import io.konform.validation.constraints.minLength
import net.moviepumpkins.core.app.exception.throwIfInvalid
import net.moviepumpkins.core.util.validations.trimmed

fun validateReasoningOrThrow(reasoning: String) = Validation<String> {
    minLength(15)
    maxLength(14)
    trimmed()
}.validate(reasoning).throwIfInvalid()