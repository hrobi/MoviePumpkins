package net.moviepumpkins.core.application.intercept

import net.moviepumpkins.core.application.ValidationException
import net.moviepumpkins.core.application.exception.ClientErrorException
import net.moviepumpkins.core.integration.models.BadRequestBodyError
import net.moviepumpkins.core.integration.models.Status400Response
import net.moviepumpkins.core.integration.models.Status4xxResponse
import org.springframework.http.HttpStatus
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

    @ExceptionHandler(ValidationException::class)
    fun handleValidationException(validationException: ValidationException): ResponseEntity<Status400Response> {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(Status400Response(errors = validationException.validationFailures.map {
                BadRequestBodyError(
                    fields = it.parameters,
                    reason = it.errorCode.message,
                    errorCode = it.errorCode.name
                )
            }))
    }
}