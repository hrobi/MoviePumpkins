package net.moviepumpkins.core.app.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.bind.ConstructorBinding

@ConfigurationProperties(prefix = "app")
data class AppProperties @ConstructorBinding constructor(
    val authServerUrlBase: String,
)