package net.moviepumpkins.core.oauth.keycloak

class BearerToken(private val token: String) {
    override fun toString(): String = "Bearer $token"
}

data class RoleRepresentation(val id: String, val name: String)

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

data class NewUserRepresentationData(
    val username: String? = null,
    val firstName: String,
    val lastName: String,
    val email: String,
    val attributes: UserRepresentation.UserAttributes?,
    val realmRoles: List<String>? = null,
    val enabled: Boolean = true,
    val emailVerified: Boolean = true
)

data class UpdateUserRepresentationData(
    val firstName: String?,
    val lastName: String?,
    val email: String?,
    val attributes: UserAttributes?
) {
    data class UserAttributes(
        val displayName: List<String>?
    )
}