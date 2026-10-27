package net.moviepumpkins.core.application.integration.keycloak

class BearerToken(private val token: String) {
    override fun toString(): String = "Bearer $token"
}