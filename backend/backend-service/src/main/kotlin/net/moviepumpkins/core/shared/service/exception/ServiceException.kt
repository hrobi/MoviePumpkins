package net.moviepumpkins.core.shared.service.exception

import org.springframework.http.HttpStatus

class ServiceException(
    val status: HttpStatus,
    val code: String,
    val details: Map<String, Any>,
    override val message: String
) : RuntimeException() {

    companion object {
        fun idConflict(code: String, idValue: String, idName: String = "id"): ServiceException = ServiceException(
            status = HttpStatus.CONFLICT,
            code = code,
            details = mapOf(idName to idValue),
            message = "Resource ${code.codeFirstComponent()} already exists with $idName equalling $idValue"
        )

        fun idNotFound(code: String, idValue: String, idName: String = "id"): ServiceException = ServiceException(
            status = HttpStatus.NOT_FOUND,
            code = code,
            details = mapOf(idName to idValue),
            message = "Resource ${code.codeFirstComponent()} is not found with $idName equalling $idValue"
        )

        private fun String.codeFirstComponent() = split(".").first()
    }

}