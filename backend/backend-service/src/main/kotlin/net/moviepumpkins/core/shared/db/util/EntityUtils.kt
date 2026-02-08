package net.moviepumpkins.core.shared.db.util

inline fun <reified T> entityEqualsBy(thiz: T, other: Any?, map: T.() -> Any): Boolean {
    if (thiz === other) return true
    if (other !is T) return false
    return thiz.map() == other.map()
}
