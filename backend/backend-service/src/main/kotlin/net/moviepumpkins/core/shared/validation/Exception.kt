package net.moviepumpkins.core.shared.validation

class ValidationException(val validationErrors: List<ValidationError>) : RuntimeException("Validation error occured") {}

fun <T> CheckResult<T>.validOrThrow(): T {
    return when (this) {
        is Invalid -> throw ValidationException(errors)
        is Valid -> value
    }
}

fun <T> runCheckOrThrow(value: T, checkingAction: Checking<T>.() -> Unit) =
    runCheck(value, checkingAction).validOrThrow()