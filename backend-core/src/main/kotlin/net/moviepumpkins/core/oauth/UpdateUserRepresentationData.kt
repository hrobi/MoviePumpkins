package net.moviepumpkins.core.oauth

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
