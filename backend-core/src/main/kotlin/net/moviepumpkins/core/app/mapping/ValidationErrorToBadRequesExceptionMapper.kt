package net.moviepumpkins.core.app.mapping

import io.konform.validation.ValidationError
import net.moviepumpkins.core.app.exception.BadRequestException
import net.moviepumpkins.core.integration.models.BadRequestBodyError
import net.moviepumpkins.core.integration.models.BadRequestResponse

fun List<ValidationError>.toBadRequestException(pathPrefix: String = "") = BadRequestException(
    BadRequestResponse(
        map {
            BadRequestBodyError(
                field = pathPrefix + it.dataPath.removePrefix("."),
                reason = it.message
            )
        }
    )
)