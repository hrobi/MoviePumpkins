package net.moviepumpkins.core

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class BackendCoreApplication

fun main(args: Array<String>) {
	runApplication<BackendCoreApplication>(*args)
}
