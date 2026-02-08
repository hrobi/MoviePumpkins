package net.moviepumpkins.core.integration.keycloak.model

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