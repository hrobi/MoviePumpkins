package net.moviepumpkins.core.feature.user.model

enum class UserRole {
    REVIEWER,
    SUPERVISOR,
    ADMIN
}

fun defaultUserRole() = UserRole.REVIEWER