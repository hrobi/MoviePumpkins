package net.moviepumpkins.core.app

import net.moviepumpkins.core.app.config.AppProperties
import net.moviepumpkins.core.app.converter.JwtToTokenWithCustomAuthoritiesConverter
import net.moviepumpkins.core.oauth.keycloak.KeycloakClient
import net.moviepumpkins.core.user.UserService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.invoke
import org.springframework.security.config.http.SessionCreationPolicy
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
                authorize(HttpMethod.POST, "/users", authenticated)
                authorize(HttpMethod.PUT, "users/*/profile", authenticated)
                authorize(anyRequest, permitAll)
            }

            oauth2ResourceServer {
                jwt {
                    jwtAuthenticationConverter = JwtToTokenWithCustomAuthoritiesConverter(userService)
                }
            }

            csrf { disable() }

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