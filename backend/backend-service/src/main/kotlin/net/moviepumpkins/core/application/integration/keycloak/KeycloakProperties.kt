package net.moviepumpkins.core.application.integration.keycloak

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.bind.ConstructorBinding

@ConfigurationProperties("moviepumpkins.integration.keycloak")
data class KeycloakProperties @ConstructorBinding constructor(
    val authServerUrlBase: String
)