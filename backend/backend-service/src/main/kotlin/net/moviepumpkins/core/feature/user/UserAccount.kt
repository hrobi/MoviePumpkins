package net.moviepumpkins.core.feature.user

import net.moviepumpkins.core.shared.auth.AuthConstants
import net.moviepumpkins.core.shared.auth.JwtMapper
import org.springframework.security.core.Authentication
import org.springframework.security.oauth2.jwt.Jwt

data class UserAccount(
    val username: String,
    val displayName: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val role: UserRole,
    val enabled: Boolean,
) {

    fun toEntity() = UserAccountEntity(
        username = username,
        firstName = firstName,
        lastName = lastName,
        email = email,
        displayName = displayName,
        role = role,
        enabled = enabled,
    )

    companion object {

        fun assemble(authentication: Authentication, entity: Lazy<UserAccountEntity>): UserAccount {
            val isNewUser =
                authentication.authorities.any { authority -> authority.authority == AuthConstants.ROLE_NEW_USER }
            return if (isNewUser) {
                JwtMapper.mapJwtToUserAccount(authentication.principal as Jwt, UserRole.default)
            } else {
                entity.value.mapToUserAccount()
            }
        }

    }
}