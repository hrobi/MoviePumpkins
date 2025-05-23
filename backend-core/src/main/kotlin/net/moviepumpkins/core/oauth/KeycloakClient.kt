package net.moviepumpkins.core.oauth

import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.service.annotation.GetExchange
import org.springframework.web.service.annotation.PostExchange

interface KeycloakClient {
    @PostExchange(
        "/realms/moviepumpkins/protocol/openid-connect/token",
        contentType = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
    )
    fun fetchToken(
        @RequestParam("grant_type") grantType: String,
        @RequestParam("client_id") clientId: String,
        @RequestParam("client_secret") clientSecret: String
    ): Map<String, String>

    @GetExchange("/admin/realms/moviepumpkins/roles/{role}")
    fun getRealmRole(
        @RequestHeader("Authorization") authorization: BearerToken,
        @PathVariable role: String
    ): RoleRepresentation

    @PostExchange(
        "/admin/realms/moviepumpkins/users/{userId}/role-mappings/realm",
        contentType = MediaType.APPLICATION_JSON_VALUE
    )
    fun addRolesToUser(
        @RequestHeader("Authorization") authorization: BearerToken,
        @PathVariable userId: String,
        @RequestBody body: List<RoleRepresentation>
    )
}