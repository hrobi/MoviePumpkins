package net.moviepumpkins.core.util

import net.moviepumpkins.core.app.model.UserAccount
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication
import java.time.Duration
import java.time.Instant

fun defaultJwtWithClaims(claims: Map<String, Any>) = Jwt(
    "NOT EMPTY",
    Instant.now(),
    Instant.now().plus(Duration.ofHours(1)),
    buildMap {
        put("not-empty-header", "not-empty")
    },
    claims
)

fun jwtAuthenticationTokenFromUserAccount(userAccount: UserAccount) =
    JwtAuthenticationToken(
        defaultJwtWithClaims(buildMap {
            put("realm_access", mapOf("roles" to listOf("app_user")))
            put("preferred_username", userAccount.username)
            put("email", userAccount.email)
            put("display_name", userAccount.displayName)
            put("name", userAccount.fullName)
        }),
        listOf(SimpleGrantedAuthority("ROLE_${userAccount.role}")),
        userAccount.username
    )

fun jwtAuthenticationByUserAccount(userAccount: UserAccount) =
    authentication(jwtAuthenticationTokenFromUserAccount(userAccount))