package net.moviepumpkins.core.shared.service.exception

sealed interface ServiceError {
    data class ResourceNotFound(val resourceName: String, val resourceValue: Any) : ServiceError
    data class SameIdResourceConflict(val idName: String, val id: Any) : ServiceError
}