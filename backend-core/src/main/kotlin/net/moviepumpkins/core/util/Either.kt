package net.moviepumpkins.core.util

sealed class Either<out F, out S>

data class Failure<F>(val error: F) : Either<F, Nothing>()
data class Success<S>(val data: S) : Either<Nothing, S>()

typealias Fallible<F> = Either<F, Unit>