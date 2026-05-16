package net.moviepumpkins.core.feature.user.model

import net.moviepumpkins.core.feature.user.entity.UserAccountEntity
import net.moviepumpkins.core.feature.user.extension.mapToUserAccount
import net.moviepumpkins.core.shared.api.mapper.JwtMapper
import net.moviepumpkins.core.shared.security.constants.AuthConstants
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
    companion object {

        fun assemble(authentication: Authentication, entity: UserAccountEntity): UserAccount =
            assemble(authentication, lazy { entity })

        fun assemble(authentication: Authentication, entity: Lazy<UserAccountEntity>): UserAccount {
            val isNewUser =
                authentication.authorities.any { authority -> authority.authority == AuthConstants.ROLE_NEW_USER }
            return if (isNewUser) {
                JwtMapper.mapJwtToUserAccount(authentication.principal as Jwt, defaultUserRole())
            } else {
                entity.value.mapToUserAccount()
            }
        }
    }
}