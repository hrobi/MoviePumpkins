package net.moviepumpkins.core.app.exception

import org.springframework.http.HttpStatus

class NotFoundException() : ClientErrorException(status = HttpStatus.NOT_FOUND)