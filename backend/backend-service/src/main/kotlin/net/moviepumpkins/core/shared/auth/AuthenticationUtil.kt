package net.moviepumpkins.core.shared.auth

import net.moviepumpkins.core.feature.user.UserAccount
import net.moviepumpkins.core.feature.user.UserAccountEntity
import net.moviepumpkins.core.feature.user.defaultUserRole
import net.moviepumpkins.core.feature.user.mapToUserAccount
import net.moviepumpkins.core.shared.auth.EmptySecurityContextException
import net.moviepumpkins.core.shared.auth.JwtMapper
import org.springframework.security.core.Authentication
import org.springframework.security.oauth2.jwt.Jwt

object AuthenticationUtil {

    fun assembleUserAccount(
        authentication: Authentication?,
        entityProvider: () -> UserAccountEntity?,
    ): UserAccount {
        authentication ?: throw EmptySecurityContextException()
        val isNewUser =
            authentication.authorities.any { authority -> authority.authority == AuthConstants.ROLE_NEW_USER }
        return if (isNewUser) {
            JwtMapper.mapJwtToUserAccount(authentication.principal as Jwt, defaultUserRole())
        } else {
            entityProvider()?.mapToUserAccount()
                ?: throw IllegalStateException("User account ${authentication.name} not found despite user not being new!")
        }
    }

}