package net.moviepumpkins.core.application.integration.keycloak

data class NewUserRepresentation(
    val username: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val emailVerified: Boolean,
    val attributes: UserAttributes?,
    val enabled: Boolean,
)
