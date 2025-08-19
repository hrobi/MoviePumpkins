package net.moviepumpkins.core.application

import net.moviepumpkins.core.application.intercept.AuthenticatedUserSyncFilter
import net.moviepumpkins.core.oauth.KeycloakClient
import net.moviepumpkins.core.user.UserService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.invoke
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.oauth2.server.resource.web.authentication.BearerTokenAuthenticationFilter
import org.springframework.security.web.SecurityFilterChain
import org.springframework.web.client.RestClient
import org.springframework.web.client.support.RestClientAdapter
import org.springframework.web.service.invoker.HttpServiceProxyFactory

@Configuration
@EnableWebSecurity
class AppConfig {

    @Bean
    fun securityFilterChain(
        http: HttpSecurity,
        userService: UserService,
    ): SecurityFilterChain {
        http {
            authorizeHttpRequests {
                authorize("/users/*/protected", authenticated)
                authorize(HttpMethod.PUT, "users/*/profile", authenticated)
                authorize(anyRequest, permitAll)
            }

            oauth2ResourceServer {
                jwt { }
            }

            csrf { disable() }

            addFilterAfter<BearerTokenAuthenticationFilter>(AuthenticatedUserSyncFilter(userService))

            sessionManagement {
                sessionCreationPolicy = SessionCreationPolicy.STATELESS
            }

        }
        return http.build()
    }

    @Bean
    fun restClient(): RestClient = RestClient.create()

    @Bean
    fun keycloakClient(appProperties: AppProperties): KeycloakClient = RestClient
        .builder()
        .baseUrl(appProperties.authServerUrlBase)
        .build()
        .let { RestClientAdapter.create(it) }
        .let { HttpServiceProxyFactory.builderFor(it).build() }
        .createClient(KeycloakClient::class.java)
}