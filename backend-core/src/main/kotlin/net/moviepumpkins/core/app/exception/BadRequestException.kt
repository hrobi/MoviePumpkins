package net.moviepumpkins.core.app.exception

import net.moviepumpkins.core.integration.models.BadRequestBodyError
import net.moviepumpkins.core.integration.models.BadRequestResponse
import org.springframework.http.HttpStatus

class BadRequestException(body: BadRequestResponse) : ClientErrorException(status = HttpStatus.BAD_REQUEST, body)

interface BadRequestExceptionBuilderScope {
    fun forField(name: String, reason: String)
}

class BadRequestExceptionBuilder : BadRequestExceptionBuilderScope {

    val badRequestErrors = mutableListOf<BadRequestBodyError>()

    override fun forField(name: String, reason: String) {
        badRequestErrors.add(BadRequestBodyError(field = name, reason = reason))
    }

}

fun buildBadRequestException(transform: BadRequestExceptionBuilderScope.() -> Unit): BadRequestException {
    val builder = BadRequestExceptionBuilder()
    builder.transform()
    return BadRequestException(BadRequestResponse(violations = builder.badRequestErrors))
}