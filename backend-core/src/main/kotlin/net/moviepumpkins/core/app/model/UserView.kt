package net.moviepumpkins.core.app.model

interface UserView {
    val username: String
    val role: UserRole
    val email: String
    val displayName: String
    val fullName: String
}