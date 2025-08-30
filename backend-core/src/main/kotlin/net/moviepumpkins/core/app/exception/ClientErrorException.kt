package net.moviepumpkins.core.app.exception

import org.springframework.http.HttpStatus

class ClientErrorException(
    val status: HttpStatus,
    val body: Any
) : RuntimeException()