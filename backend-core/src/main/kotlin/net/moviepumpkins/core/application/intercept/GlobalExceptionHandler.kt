package net.moviepumpkins.core.application.intercept

import net.moviepumpkins.core.application.exception.ClientErrorException
import net.moviepumpkins.core.integration.models.Status4xxResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(ClientErrorException::class)
    fun handleClientErrorException(clientErrorException: ClientErrorException): ResponseEntity<Status4xxResponse> {
        return ResponseEntity.status(clientErrorException.status)
            .body(Status4xxResponse(reason = clientErrorException.reason))
    }
}