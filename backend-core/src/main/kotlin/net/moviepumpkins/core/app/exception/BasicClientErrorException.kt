package net.moviepumpkins.core.app.exception

open class BasicClientErrorException(
    val reason: String,
    val status: Int,
) : RuntimeException(reason)