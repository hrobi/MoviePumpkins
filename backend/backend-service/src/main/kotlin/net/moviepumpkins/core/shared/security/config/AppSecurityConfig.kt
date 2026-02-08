package net.moviepumpkins.core.shared.security.config

import net.moviepumpkins.core.shared.security.converter.JwtToTokenWithCustomAuthoritiesConverter
import net.moviepumpkins.core.feature.user.service.SimpleUserPersistenceService
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.invoke
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity
class AppSecurityConfig {

    @Bean
    fun securityFilterChain(
        http: HttpSecurity,
        simpleUserPersistenceService: SimpleUserPersistenceService,
    ): SecurityFilterChain {
        http {
            authorizeHttpRequests {
                authorize(HttpMethod.POST, "/users", authenticated)
                authorize(HttpMethod.PUT, "/users/*/profile", authenticated)
                authorize(HttpMethod.POST, "/media/*/reviews", authenticated)
                authorize(HttpMethod.PUT, "/reviews/*", authenticated)
                authorize(anyRequest, permitAll)
            }

            oauth2ResourceServer {
                jwt {
                    jwtAuthenticationConverter = JwtToTokenWithCustomAuthoritiesConverter(simpleUserPersistenceService)
                }
            }

            csrf { disable() }

            sessionManagement {
                sessionCreationPolicy = SessionCreationPolicy.STATELESS
            }

        }
        return http.build()
    }
}