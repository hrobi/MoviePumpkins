package net.moviepumpkins.core.utils

import org.springframework.security.oauth2.jwt.Jwt
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