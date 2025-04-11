package net.moviepumpkins.core.config

import jakarta.validation.ConstraintViolation

class ResponseValidationException(
    val constraintViolations: Set<ConstraintViolation<Any>>,
) : IllegalStateException()