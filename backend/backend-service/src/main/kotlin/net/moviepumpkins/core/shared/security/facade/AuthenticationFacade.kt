package net.moviepumpkins.core.shared.security.facade

import net.moviepumpkins.core.feature.user.extension.mapToUserAccount
import net.moviepumpkins.core.feature.user.model.UserAccount
import net.moviepumpkins.core.feature.user.model.defaultUserRole
import net.moviepumpkins.core.feature.user.service.SimpleUserPersistenceService
import net.moviepumpkins.core.shared.api.mapper.JwtMapper
import net.moviepumpkins.core.shared.security.constants.AuthConstants
import net.moviepumpkins.core.shared.security.exception.EmptySecurityContextException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.stereotype.Component

@Component
class AuthenticationFacade(
    private val simpleUserPersistenceService: SimpleUserPersistenceService,
    private val jwtMapper: JwtMapper
) {

    fun extractUserAccount(): UserAccount {
        val authentication = SecurityContextHolder.getContext().authentication ?: throw EmptySecurityContextException()
        val isNewUser =
            authentication.authorities!!.any { authority -> authority.authority == AuthConstants.ROLE_NEW_USER }
        return if (isNewUser) {
            jwtMapper.mapJwtToUserAccount(authentication.principal as Jwt, userRole = defaultUserRole())
        } else {
            simpleUserPersistenceService.getUserAccountEntity(username = authentication.name)?.mapToUserAccount()
                ?: throw IllegalStateException("User account ${authentication.name} not found despite user not being new!")
        }
    }

}