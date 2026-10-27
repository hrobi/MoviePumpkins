package net.moviepumpkins.core.application.integration.keycloak

data class PasswordSetterRequest(
    val type: String,
    val value: String,
    val temporary: Boolean,
)

fun createPermanentPasswordSetterRequest(password: String) = PasswordSetterRequest(
    type = "password",
    value = password,
    temporary = false,
)