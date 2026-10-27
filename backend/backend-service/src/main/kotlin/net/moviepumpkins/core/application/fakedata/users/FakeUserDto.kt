package net.moviepumpkins.core.application.fakedata.users

import net.moviepumpkins.core.feature.user.UserRole

data class FakeUserDto(
    val username: String,
    val displayName: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val role: UserRole,
)