package net.moviepumpkins.core.feature.user.util

import net.moviepumpkins.core.shared.service.exception.ServiceError
import net.moviepumpkins.core.shared.service.exception.ServiceException

fun throwUserNotFoundServiceException(username: String, code: String? = null): Nothing {
    throw ServiceException(
        ServiceError.ResourceNotFound("username", username),
        code = code ?: "user.notFound",
        message = "Username $username not found",
    )
}