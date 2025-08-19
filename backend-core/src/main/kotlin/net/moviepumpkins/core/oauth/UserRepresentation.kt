package net.moviepumpkins.core.oauth

data class UserRepresentation(
    val id: String,
    val username: String? = null,
    val firstName: String,
    val lastName: String,
    val email: String,
    val attributes: UserAttributes?
) {
    data class UserAttributes(
        val displayName: List<String>?
    )
}