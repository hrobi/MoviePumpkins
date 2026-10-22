package net.moviepumpkins.core.feature.user

enum class UserRole {
    REVIEWER,
    SUPERVISOR,
    ADMIN
}

fun defaultUserRole() = UserRole.REVIEWER