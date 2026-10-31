package net.moviepumpkins.core.feature.user

import net.moviepumpkins.core.shared.Loader
import net.moviepumpkins.core.shared.auth.AuthConstants
import net.moviepumpkins.core.shared.auth.JwtMapper
import org.springframework.security.core.Authentication
import org.springframework.security.oauth2.jwt.Jwt

data class UserAccountDetails(
    val username: String,
    val displayName: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val role: UserRole,
    val enabled: Boolean,
) {

    fun toEntity() = UserAccount(
        username = username,
        firstName = firstName,
        lastName = lastName,
        email = email,
        displayName = displayName,
        role = role,
        enabled = enabled,
    )

    companion object {

        fun compose(persistenceLoader: Loader<UserAccount?>, authentication: Authentication): UserAccountDetails {
            val isNewUser =
                authentication.authorities.any { authority -> authority.authority == AuthConstants.ROLE_NEW_USER }
            return if (isNewUser) {
                JwtMapper.mapJwtToUserAccount(authentication.principal as Jwt, UserRole.default)
            } else {
                persistenceLoader.load()
                    ?.mapToUserAccount()
                    ?: throw IllegalStateException("user was not found")
            }
        }

    }

}