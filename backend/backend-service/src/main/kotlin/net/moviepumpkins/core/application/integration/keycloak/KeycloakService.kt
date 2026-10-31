package net.moviepumpkins.core.application.integration.keycloak

import net.moviepumpkins.core.feature.user.UserAccountDetails
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository
import org.springframework.stereotype.Component

@Component
class KeycloakService(
    clientRegistrationRepository: ClientRegistrationRepository,
    private val keycloakClient: KeycloakClient,
) {

    private final val clientId: String
    private final val clientSecret: String

    init {
        val keycloakClient = clientRegistrationRepository.findByRegistrationId("keycloak")
        clientId = keycloakClient.clientId
        clientSecret = keycloakClient.clientSecret
    }

    private fun getAdminAccessToken(): BearerToken {
        return BearerToken(
            keycloakClient.fetchToken(
                grantType = "client_credentials",
                clientId = clientId,
                clientSecret = clientSecret
            )["access_token"]!!
        )
    }


    fun createEmailVerifiedAndEnabledUser(userAccount: UserAccountDetails, password: String) {
        val adminAccessToken = getAdminAccessToken()
        val userCreatedResponse = keycloakClient.createUser(
            adminAccessToken,
            with(userAccount) {
                NewUserRepresentation(
                    username = username,
                    firstName = firstName,
                    lastName = lastName,
                    email = email,
                    emailVerified = true,
                    attributes = UserAttributes(displayName = listOf(displayName)),
                    enabled = true,
                )
            }
        )
        val userIdFromLocationHeader = userCreatedResponse.headers.get("Location")?.get(0)?.split("/")?.last()
            ?: throw IllegalStateException("Location header expected from keycloak when creating user")
        keycloakClient.setPasswordForUser(
            adminAccessToken,
            userIdFromLocationHeader,
            createPermanentPasswordSetterRequest(password)
        )
    }
}