package net.moviepumpkins.core.oauth

import net.moviepumpkins.core.general.getLogger
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository
import org.springframework.stereotype.Service
import org.springframework.web.client.RestClient

@Service
class AuthorizationService(
    private val restClient: RestClient,
    private val keycloakClient: KeycloakClient,
    clientRegistrationRepository: ClientRegistrationRepository,
) {

    private val LOG = getLogger()

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

    fun addAppUserRoleByUserId(userId: String) {
        val adminToken = fetchAdminToken()!!
        val role = keycloakClient.getRealmRole(authorization = BearerToken(adminToken), "app_user")
        keycloakClient.addRolesToUser(authorization = BearerToken(adminToken), userId = userId, listOf(role))
    }
}