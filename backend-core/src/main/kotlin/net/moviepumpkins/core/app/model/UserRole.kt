package net.moviepumpkins.core.app.model

enum class UserRole(val level: Int) {
    REVIEWER(1),
    SUPERVISOR(2),
    ADMIN(3);

    fun isAtLeast(userRole: UserRole) = level > userRole.level
}