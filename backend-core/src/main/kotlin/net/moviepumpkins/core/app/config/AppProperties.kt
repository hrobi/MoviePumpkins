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

@ConfigurationProperties(prefix = "app.paged")
data class PagedProperties @ConstructorBinding constructor(
    val pageSize: Int,
) {
    fun derivePageCount(count: Int) = count / pageSize + if (count % pageSize == 0) 0 else 1
}

@ConfigurationProperties(prefix = "app.scoring")
data class ScoringProperties @ConstructorBinding constructor(
    val minScore: Float,
    val maxScore: Float,
)