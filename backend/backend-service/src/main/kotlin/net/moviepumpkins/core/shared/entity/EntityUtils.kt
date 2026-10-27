package net.moviepumpkins.core.shared.entity

import org.springframework.data.jpa.repository.JpaRepository

inline fun <reified T> entityEqualsBy(thiz: T, other: Any?, map: T.() -> Any): Boolean {
    if (thiz === other) return true
    if (other !is T) return false
    return thiz.map() == other.map()
}

fun <E, ID> JpaRepository<E, ID & Any>.find(id: ID & Any): E? = findById(id).orElse(null)