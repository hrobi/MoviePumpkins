package net.moviepumpkins.core.user

interface SimpleUserView {
    val username: String
    val email: String
    val fullName: String
    val role: UserRole
}