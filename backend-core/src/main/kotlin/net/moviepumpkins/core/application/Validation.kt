package net.moviepumpkins.core.application

data class ValidationFailure(val parameters: List<String>, val errorCode: ErrorCode)

class ValidationException(val validationFailures: List<ValidationFailure>) : RuntimeException("Validation failed") {}

class ValidationDSL {

    private val validationFailuresMutable: MutableList<ValidationFailure> = mutableListOf()

    val validationFailures: List<ValidationFailure>
        get() = validationFailuresMutable

    fun validateSingleParameter(parameterName: String, errorCode: ErrorCode, check: () -> Boolean) {
        if (!check()) {
            validationFailuresMutable.add(ValidationFailure(parameters = listOf(parameterName), errorCode))
        }
    }
}


fun validations(validate: ValidationDSL.() -> Unit) {
    val validationDSL = ValidationDSL()
    validationDSL.validate()
    if (validationDSL.validationFailures.isNotEmpty()) {
        throw ValidationException(validationDSL.validationFailures)
    }
}
