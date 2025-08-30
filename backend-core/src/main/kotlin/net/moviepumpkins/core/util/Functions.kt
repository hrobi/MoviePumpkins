package net.moviepumpkins.core.util

import net.moviepumpkins.core.app.model.UserAccount
import org.springframework.security.core.context.SecurityContextHolder

inline fun <reified T> equalsBy(thiz: T, other: Any?, map: T.() -> Any): Boolean {
    if (thiz === other) return true
    if (other !is T) return false
    return thiz.map() == other.map()
}

fun getAuthentication() = SecurityContextHolder.getContext().authentication as UserAccount