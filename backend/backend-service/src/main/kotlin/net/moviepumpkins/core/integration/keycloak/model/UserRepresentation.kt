package net.moviepumpkins.core.integration.keycloak.model

data class UserRepresentation(
    val id: String,
    val username: String? = null,
    val firstName: String,
    val lastName: String,
    val email: String,
    val attributes: UserAttributes?
)