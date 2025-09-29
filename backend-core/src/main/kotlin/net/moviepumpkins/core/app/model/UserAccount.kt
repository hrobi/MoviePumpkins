package net.moviepumpkins.core.app.model

data class UserAccount(
    override val username: String,
    override val role: UserRole,
    override val email: String,
    override val displayName: String,
    override val fullName: String,
    val isAppUser: Boolean,
) : UserView