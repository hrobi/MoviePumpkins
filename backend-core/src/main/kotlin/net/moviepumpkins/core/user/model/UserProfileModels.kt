package net.moviepumpkins.core.user.model

data class UserProfileUpdate(
    val fullName: String,
    val displayName: String,
    val email: String
)