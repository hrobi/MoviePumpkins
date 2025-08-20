package net.moviepumpkins.core.oauth

import net.moviepumpkins.core.oauth.keycloak.UpdateUserRepresentationData
import net.moviepumpkins.core.oauth.keycloak.UseKeycloakAsAdminService
import net.moviepumpkins.core.oauth.keycloak.UserRepresentation
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository
import org.springframework.stereotype.Service

@Service
class AuthorizationService(
    private val useKeycloakAsAdmin: UseKeycloakAsAdminService,
    clientRegistrationRepository: ClientRegistrationRepository,
) {

    private final val clientId: String
    private final val clientSecret: String

    init {
        val keycloakClient = clientRegistrationRepository.findByRegistrationId("keycloak")
        clientId = keycloakClient.clientId
        clientSecret = keycloakClient.clientSecret
    }

    fun addAppUserRoleBySid(sid: String) {
        useKeycloakAsAdmin {
            val role = keycloakClient.getRealmRole(authorization = adminAccessToken, "app_user")
            keycloakClient.addRolesToUser(authorization = adminAccessToken, userId = sid, listOf(role))
        }
    }

    fun updateUserByUsername(username: String, updateUserData: UpdateUserRepresentationData) {
        useKeycloakAsAdmin {
            val user = keycloakClient.getUser(adminAccessToken, username)[0]
            keycloakClient.updateUser(adminAccessToken, user.id, UserRepresentation(
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
}