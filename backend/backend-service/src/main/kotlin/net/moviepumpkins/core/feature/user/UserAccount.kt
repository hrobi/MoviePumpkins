package net.moviepumpkins.core.feature.user

import net.moviepumpkins.core.shared.auth.AuthConstants
import net.moviepumpkins.core.shared.auth.JwtMapper
import org.springframework.security.core.Authentication
import org.springframework.security.oauth2.jwt.Jwt
import java.util.function.Supplier

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

        fun compose(authentication: Authentication, entitySupplier: Supplier<UserAccountEntity?>): UserAccount {
            val isNewUser =
                authentication.authorities.any { authority -> authority.authority == AuthConstants.ROLE_NEW_USER }
            return if (isNewUser) {
                JwtMapper.mapJwtToUserAccount(authentication.principal as Jwt, UserRole.default)
            } else {
                val entity = entitySupplier.get()
                entity
                    ?.mapToUserAccount()
                    ?: throw IllegalStateException("user was not found")
            }
        }

    }

}