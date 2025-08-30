package net.moviepumpkins.core.util

import net.moviepumpkins.core.app.exception.BasicClientErrorException

fun throwUnauthorized(reason: String) {
    throw BasicClientErrorException(reason, 403)
}