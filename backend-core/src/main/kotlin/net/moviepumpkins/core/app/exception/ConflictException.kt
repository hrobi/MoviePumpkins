package net.moviepumpkins.core.app.exception

import org.springframework.http.HttpStatus

class ConflictException : ClientErrorException(HttpStatus.CONFLICT)