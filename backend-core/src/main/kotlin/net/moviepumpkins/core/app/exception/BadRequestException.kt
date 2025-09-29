package net.moviepumpkins.core.app.exception

import net.moviepumpkins.core.integration.models.BadRequestBodyError
import net.moviepumpkins.core.integration.models.Status400Response
import net.moviepumpkins.core.util.PropertyCheck
import org.springframework.http.HttpStatus

class BadRequestException(body: Status400Response) : ClientErrorException(status = HttpStatus.BAD_REQUEST, body)

interface BadRequestBuilderScope {
    operator fun PropertyCheck.unaryPlus()
}

class BadRequestBuilder : BadRequestBuilderScope {
    private val errors: MutableList<BadRequestBodyError> = mutableListOf()
    override operator fun PropertyCheck.unaryPlus() {
        if (matches) {
            return
        }
        errors.add(BadRequestBodyError(propertyPaths.split(","), errorMessageIfNotMatching))
    }

    fun violationsOccured() = errors.isNotEmpty()

    fun throwBadRequestException() {
        throw BadRequestException(Status400Response(errors))
    }
}

fun checkRequest(builder: BadRequestBuilderScope.() -> Unit) {
    val badRequestBuilder = BadRequestBuilder()
    badRequestBuilder.builder()
    if (badRequestBuilder.violationsOccured()) {
        badRequestBuilder.throwBadRequestException()
    }
}