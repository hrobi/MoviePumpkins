package net.moviepumpkins.core.util

inline fun <reified T> equalsBy(thiz: T, other: Any?, map: T.() -> Any): Boolean {
    if (thiz === other) return true
    if (other !is T) return false
    return thiz.map() == other.map()
}

fun getPageCount(count: Int, pageSize: Int) = count / pageSize + if (count % pageSize > 0) 1 else 0