package net.moviepumpkins.core

import net.moviepumpkins.core.application.AppProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableConfigurationProperties(AppProperties::class)
class BackendCoreApplication

fun main(args: Array<String>) {
    runApplication<BackendCoreApplication>(*args)
}
