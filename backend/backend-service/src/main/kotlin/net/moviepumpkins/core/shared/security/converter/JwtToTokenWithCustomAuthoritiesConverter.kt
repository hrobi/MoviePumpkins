package net.moviepumpkins.core.shared.security.converter

import net.moviepumpkins.core.feature.user.model.defaultUserRole
import net.moviepumpkins.core.feature.user.service.SimpleUserPersistenceService
import net.moviepumpkins.core.shared.logging.extension.lazyDebug
import net.moviepumpkins.core.shared.logging.util.logger
import net.moviepumpkins.core.shared.security.constants.AuthConstants
import net.moviepumpkins.core.shared.security.extension.mapToSimpleGrantedAuthority
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
                    add(defaultUserRole().mapToSimpleGrantedAuthority())
                    add(SimpleGrantedAuthority(AuthConstants.ROLE_NEW_USER))
                }
            },
            username
        )
    }
}