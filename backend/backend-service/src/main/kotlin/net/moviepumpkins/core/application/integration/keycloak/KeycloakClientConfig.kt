package net.moviepumpkins.core.application.integration.keycloak

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestClient
import org.springframework.web.client.support.RestClientAdapter
import org.springframework.web.service.invoker.HttpServiceProxyFactory

@Configuration
class KeycloakClientConfig {
    @Bean
    fun restClient(): RestClient = RestClient.create()

    @Bean
    fun keycloakClient(keycloakProperties: KeycloakProperties): KeycloakClient {
        return RestClient
            .builder()
            .baseUrl(keycloakProperties.authServerUrlBase)
            .build()
            .let { RestClientAdapter.create(it) }
            .let { HttpServiceProxyFactory.builderFor(it).build() }
            .createClient(KeycloakClient::class.java)
    }
}