package net.moviepumpkins.core.application.exception

open class ClientErrorException(
    val reason: String,
    val status: Int,
) : RuntimeException(reason)