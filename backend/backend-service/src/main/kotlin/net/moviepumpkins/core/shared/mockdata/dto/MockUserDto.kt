package net.moviepumpkins.core.shared.mockdata.dto

import net.moviepumpkins.core.feature.user.model.UserRole

data class MockUserDto(
    val username: String,
    val displayName: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val role: UserRole,
)