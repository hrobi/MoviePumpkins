package net.moviepumpkins.core.utils

inline fun <reified T> equalsBy(thiz: T, other: Any?, map: T.() -> Any): Boolean {
    if (thiz === other) return true
    if (other !is T) return false
    return thiz.map() == other.map()
}