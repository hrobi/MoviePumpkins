package net.moviepumpkins.core.integration.keycloak.client

import net.moviepumpkins.core.integration.keycloak.model.BearerToken
import net.moviepumpkins.core.integration.keycloak.model.NewUserRepresentation
import net.moviepumpkins.core.integration.keycloak.model.PasswordSetterRequest
import net.moviepumpkins.core.integration.keycloak.model.UserRepresentation
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.service.annotation.GetExchange
import org.springframework.web.service.annotation.PostExchange
import org.springframework.web.service.annotation.PutExchange

interface KeycloakClient {

    @GetExchange(
        "/admin/realms/moviepumpkins/users?username={username}"
    )
    fun getUser(
        @RequestHeader("Authorization") authorization: BearerToken,
        @PathVariable("username") username: String,
    ): List<UserRepresentation>

    @PostExchange(
        "/realms/moviepumpkins/protocol/openid-connect/token",
        contentType = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
    )
    fun fetchToken(
        @RequestParam("grant_type") grantType: String,
        @RequestParam("client_id") clientId: String,
        @RequestParam("client_secret") clientSecret: String,
    ): Map<String, String>

    @PostExchange(
        value = "/admin/realms/moviepumpkins/users",
        contentType = MediaType.APPLICATION_JSON_VALUE,
    )
    fun createUser(
        @RequestHeader("Authorization") authorization: BearerToken,
        @RequestBody user: NewUserRepresentation
    ): ResponseEntity<Unit>

    @PutExchange(
        value = "/admin/realms/moviepumpkins/users/{userId}/reset-password"
    )
    fun setPasswordForUser(
        @RequestHeader("Authorization") authorization: BearerToken,
        @PathVariable("userId") userId: String,
        @RequestBody user: PasswordSetterRequest
    )
}