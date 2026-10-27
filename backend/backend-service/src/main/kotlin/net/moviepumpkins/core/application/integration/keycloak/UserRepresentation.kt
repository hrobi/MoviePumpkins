package net.moviepumpkins.core.application.integration.keycloak

data class UserRepresentation(
    val id: String,
    val username: String? = null,
    val firstName: String,
    val lastName: String,
    val email: String,
    val attributes: UserAttributes?
)