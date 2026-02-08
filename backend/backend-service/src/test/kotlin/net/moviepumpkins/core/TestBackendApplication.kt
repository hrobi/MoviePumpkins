package net.moviepumpkins.core

import org.springframework.boot.fromApplication


fun main(args: Array<String>) {
    fromApplication<BackendApplication>().run(*args)
}
