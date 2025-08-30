package net.moviepumpkins.core.app.exception

import jakarta.validation.ConstraintViolation

class ResponseValidationException(
    val constraintViolations: Set<ConstraintViolation<Any>>,
) : IllegalStateException()