package net.moviepumpkins.core.oauth.keycloak

import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository
import org.springframework.stereotype.Service

@Service
class UseKeycloakAsAdminService(
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

    class KeycloakClientDSL(
        val keycloakClient: KeycloakClient,
        clientId: String,
        clientSecret: String
    ) {
        val adminAccessToken by lazy {
            BearerToken(
                keycloakClient.fetchToken(
                    grantType = "client_credentials",
                    clientId = clientId,
                    clientSecret = clientSecret
                )["access_token"]!!
            )
        }
    }

    operator fun invoke(useKeycloakAsAdmin: KeycloakClientDSL.() -> Unit) {
        KeycloakClientDSL(keycloakClient, clientId, clientSecret).useKeycloakAsAdmin()
    }
}