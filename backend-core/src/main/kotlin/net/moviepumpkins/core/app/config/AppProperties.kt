package net.moviepumpkins.core.app.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.bind.ConstructorBinding

@ConfigurationProperties(prefix = "app")
data class AppProperties @ConstructorBinding constructor(
    val authServerUrlBase: String,
    val serverUrlBase: String,
    val posterImage: PosterImageProperties,
)

@ConfigurationProperties(prefix = "app.poster-image")
data class PosterImageProperties @ConstructorBinding constructor(
    val maxSizeInBytes: Long,
    val widthInPx: Int,
)

@ConfigurationProperties(prefix = "app.reviews")
data class ReviewsProperties @ConstructorBinding constructor(
    val pageSize: Int,
)