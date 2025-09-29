package net.moviepumpkins.core.app.exception

import org.springframework.http.HttpStatus

open class ClientErrorException(
    val status: HttpStatus,
    val body: Any? = null
) : RuntimeException()