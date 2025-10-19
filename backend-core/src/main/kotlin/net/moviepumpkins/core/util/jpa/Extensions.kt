package net.moviepumpkins.core.util.jpa

import org.springframework.data.jpa.repository.JpaRepository

inline fun <T, ID> JpaRepository<T, ID>.getReferenceByIdOrThrow(
    id: ID & Any,
    exceptionBuilder: () -> RuntimeException,
): T {
    if (!existsById(id)) {
        throw exceptionBuilder()
    }

    return getReferenceById(id)
}