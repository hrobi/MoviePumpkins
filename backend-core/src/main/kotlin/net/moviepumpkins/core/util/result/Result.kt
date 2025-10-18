package net.moviepumpkins.core.util.result

sealed interface Result<out T, out E> {
    fun <F> mapFailure(transform: (E) -> F): Result<T, F> {
        return when (this) {
            is Failure -> Failure(transform(this.cause))
            is Success -> this
        }
    }
}

interface FailureMappable<T> {
    fun toFailure(): Failure<T> = Failure(this as T)
}

inline fun <T, E> Result<T, E>.succeedOrElse(action: Failure<E>.() -> Unit): T {
    if (this is Failure) {
        action(this)
        throw IllegalStateException("${Result::class.simpleName} should have been ${Success::class.simpleName}")
    }

    return (this as Success<T>).value
}

data class Success<T>(val value: T) : Result<T, Nothing>
data class Failure<E>(val cause: E) : Result<Nothing, E>