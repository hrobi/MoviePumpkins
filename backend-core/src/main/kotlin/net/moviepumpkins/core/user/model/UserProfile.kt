package net.moviepumpkins.core.user.model

data class UserProfile(
    val username: String,
    val fullName: String,
    val email: String,
    val displayName: String,
    val role: UserRole,
)