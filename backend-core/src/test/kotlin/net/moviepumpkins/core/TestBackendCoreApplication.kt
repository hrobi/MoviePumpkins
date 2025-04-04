package net.moviepumpkins.core

import org.springframework.boot.fromApplication
import org.springframework.boot.with


fun main(args: Array<String>) {
	fromApplication<BackendCoreApplication>().with(TestcontainersConfiguration::class).run(*args)
}
