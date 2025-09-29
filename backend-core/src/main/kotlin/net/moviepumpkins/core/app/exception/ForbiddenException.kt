package net.moviepumpkins.core.app.exception

import org.springframework.http.HttpStatus

class ForbiddenException : ClientErrorException(HttpStatus.FORBIDDEN)