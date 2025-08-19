package net.moviepumpkins.core.utils

import net.moviepumpkins.core.application.exception.ClientErrorException

fun throwUnauthorized(reason: String) {
    throw ClientErrorException(reason, 403)
}