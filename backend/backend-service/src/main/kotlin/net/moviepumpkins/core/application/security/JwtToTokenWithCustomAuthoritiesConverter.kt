package net.moviepumpkins.core.application.security

import net.moviepumpkins.core.feature.user.UserRole
import net.moviepumpkins.core.feature.user.application.SimpleUserPersistenceService
import net.moviepumpkins.core.shared.auth.AuthConstants
import net.moviepumpkins.core.shared.auth.mapToSimpleGrantedAuthority
import net.moviepumpkins.core.shared.lazyDebug
import net.moviepumpkins.core.shared.logger
import org.springframework.core.convert.converter.Converter
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken

class JwtToTokenWithCustomAuthoritiesConverter(
    private val simpleUserPersistenceService: SimpleUserPersistenceService,
) : Converter<Jwt, JwtAuthenticationToken> {

    override fun convert(source: Jwt): JwtAuthenticationToken {
        val username = source.getClaimAsString("preferred_username")
        logger().lazyDebug { "JWT username: $username" }
        val role = simpleUserPersistenceService.getUserAccountEntity(username)?.role
        return JwtAuthenticationToken(
            source,
            buildList {
                if (role != null) {
                    add(role.mapToSimpleGrantedAuthority())
                } else {
                    add(UserRole.default.mapToSimpleGrantedAuthority())
                    add(SimpleGrantedAuthority(AuthConstants.ROLE_NEW_USER))
                }
            },
            username
        )
    }
}