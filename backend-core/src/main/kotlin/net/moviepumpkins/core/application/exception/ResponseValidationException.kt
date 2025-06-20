package net.moviepumpkins.core.application.exception

import jakarta.validation.ConstraintViolation

class ResponseValidationException(
    val constraintViolations: Set<ConstraintViolation<Any>>,
) : IllegalStateException()