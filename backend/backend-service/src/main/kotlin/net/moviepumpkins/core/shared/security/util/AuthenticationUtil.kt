package net.moviepumpkins.core.shared.security.util

import net.moviepumpkins.core.feature.user.entity.UserAccountEntity
import net.moviepumpkins.core.feature.user.extension.mapToUserAccount
import net.moviepumpkins.core.feature.user.model.UserAccount
import net.moviepumpkins.core.feature.user.model.defaultUserRole
import net.moviepumpkins.core.shared.api.mapper.JwtMapper
import net.moviepumpkins.core.shared.security.constants.AuthConstants
import net.moviepumpkins.core.shared.security.exception.EmptySecurityContextException
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