package net.moviepumpkins.core.shared.auth

import org.springframework.security.core.context.SecurityContextHolder

fun getAuthUsernameOrThrow(): String {
    val authentication = SecurityContextHolder.getContext().authentication
    if (!authentication.isAuthenticated) {
        throw IllegalStateException("No authenticated user")
    } else {
        return authentication.name
    }
}

fun getAuthUsernameOrNull(): String? {
    val authentication = SecurityContextHolder.getContext().authentication
    return if (!authentication.isAuthenticated) {
        null
    } else {
        authentication.name
    }
}
