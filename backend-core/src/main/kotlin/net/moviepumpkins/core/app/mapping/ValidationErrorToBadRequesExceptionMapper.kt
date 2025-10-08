package net.moviepumpkins.core.app.mapping

import io.konform.validation.ValidationError
import net.moviepumpkins.core.app.exception.BadRequestException
import net.moviepumpkins.core.integration.models.BadRequestBodyError
import net.moviepumpkins.core.integration.models.Status400Response

fun List<ValidationError>.toBadRequestException(pathPrefix: String = "") = BadRequestException(
    Status400Response(
        map {
            BadRequestBodyError(
                fields = listOf(pathPrefix + it.dataPath.removePrefix(".")),
                reason = it.message
            )
        }
    )
)