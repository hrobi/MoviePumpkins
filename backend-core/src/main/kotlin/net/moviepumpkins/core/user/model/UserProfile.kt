package net.moviepumpkins.core.user.model

data class UserProfile(
    val username: String,
    val fullName: String,
    val email: String,
    val displayName: String,
    val about: String = "",
    val role: UserRole,
)