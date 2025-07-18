package net.moviepumpkins.core.user.db

import net.moviepumpkins.core.user.model.UserRole

interface SimpleUserView {
    val username: String
    val displayName: String
    val email: String
    val fullName: String
    val role: UserRole
}