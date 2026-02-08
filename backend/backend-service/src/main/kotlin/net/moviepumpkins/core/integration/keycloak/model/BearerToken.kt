package net.moviepumpkins.core.integration.keycloak.model

class BearerToken(private val token: String) {
    override fun toString(): String = "Bearer $token"
}