package net.moviepumpkins.core.app.converter

import net.moviepumpkins.core.app.model.UserRole
import net.moviepumpkins.core.user.UserService
import org.springframework.core.convert.converter.Converter
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken

class JwtToTokenWithCustomAuthoritiesConverter(
    private val userService: UserService
) : Converter<Jwt, JwtAuthenticationToken> {
    override fun convert(source: Jwt): JwtAuthenticationToken {
        val username = source.getClaimAsString("preferred_username")
        val userRole = userService.getUserRoleByUsername(username)
        return JwtAuthenticationToken(
            source,
            listOf(SimpleGrantedAuthority("ROLE_${userRole ?: UserRole.REVIEWER}")),
            username
        )
    }
}