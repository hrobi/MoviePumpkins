package net.moviepumpkins.core.oauth

import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository
import org.springframework.stereotype.Service

@Service
class AuthorizationService(
    private val keycloakClient: KeycloakClient,
    clientRegistrationRepository: ClientRegistrationRepository,
) {

    private final val clientId: String
    private final val clientSecret: String

    init {
        val keycloakClient = clientRegistrationRepository.findByRegistrationId("keycloak")
        clientId = keycloakClient.clientId
        clientSecret = keycloakClient.clientSecret
    }

    private fun fetchAdminToken(): String? {
        val keycloakResponseAsMap = keycloakClient.fetchToken(
            grantType = "client_credentials",
            clientId = clientId,
            clientSecret = clientSecret
        )

        return keycloakResponseAsMap["access_token"]
    }

    class WithAdminTokenScope(val adminToken: BearerToken) {}

    private fun <T> withAdminToken(doAction: WithAdminTokenScope.() -> T) {
        val adminToken = fetchAdminToken()!!
        WithAdminTokenScope(BearerToken(adminToken)).doAction()
    }

    fun addAppUserRoleBySid(sid: String) = withAdminToken {
        val role = keycloakClient.getRealmRole(authorization = adminToken, "app_user")
        keycloakClient.addRolesToUser(authorization = adminToken, userId = sid, listOf(role))
    }

    fun updateUserByUsername(username: String, updateUserData: UpdateUserRepresentationData) = withAdminToken {
        val user = keycloakClient.getUser(adminToken, username)[0]
        keycloakClient.updateUser(adminToken, user.id, UserRepresentation(
            id = user.id,
            firstName = updateUserData.firstName ?: user.firstName,
            lastName = updateUserData.lastName ?: user.lastName,
            email = updateUserData.email ?: user.email,
            attributes = updateUserData.attributes?.let {
                UserRepresentation.UserAttributes(
                    listOf(
                        it.displayName!!.get(
                            0
                        )
                    )
                )
            }
        ))
    }
}