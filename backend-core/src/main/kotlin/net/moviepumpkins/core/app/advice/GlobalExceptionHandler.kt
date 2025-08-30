package net.moviepumpkins.core.app.advice

import net.moviepumpkins.core.app.exception.BasicClientErrorException
import net.moviepumpkins.core.app.exception.ClientErrorException
import net.moviepumpkins.core.integration.models.Status4xxResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(BasicClientErrorException::class)
    fun handleBasicClientErrorException(basicClientErrorException: BasicClientErrorException): ResponseEntity<Status4xxResponse> {
        return ResponseEntity.status(basicClientErrorException.status)
            .body(Status4xxResponse(reason = basicClientErrorException.reason))
    }

    @ExceptionHandler(ClientErrorException::class)
    fun handleClientErrorException(clientErrorException: ClientErrorException): ResponseEntity<Any> {
        return ResponseEntity.status(clientErrorException.status).body(clientErrorException.body);
    }
}