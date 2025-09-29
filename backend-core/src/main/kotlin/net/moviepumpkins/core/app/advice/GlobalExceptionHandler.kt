package net.moviepumpkins.core.app.advice

import net.moviepumpkins.core.app.exception.ClientErrorException
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(ClientErrorException::class)
    fun handleClientErrorException(clientErrorException: ClientErrorException): ResponseEntity<Any> {
        return ResponseEntity.status(clientErrorException.status).body(clientErrorException.body)
    }
}