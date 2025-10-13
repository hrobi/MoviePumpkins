package net.moviepumpkins.core

import net.moviepumpkins.core.app.config.AppProperties
import net.moviepumpkins.core.app.config.PagedProperties
import net.moviepumpkins.core.app.config.PosterImageProperties
import net.moviepumpkins.core.app.config.ScoringProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication
@ComponentScan(basePackages = ["net.moviepumpkins"])
@EnableConfigurationProperties(
    AppProperties::class,
    PosterImageProperties::class,
    PagedProperties::class,
    ScoringProperties::class
)
class BackendCoreApplication

fun main(args: Array<String>) {
    runApplication<BackendCoreApplication>(*args)
}
