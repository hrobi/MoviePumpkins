package net.moviepumpkins.core.media.scores.exception

class ScoringStateException(val state: ScoringStateError) : RuntimeException() {
    companion object {
        fun mediaDoesNotExist() = ScoringStateException(ScoringStateError.MediaDoesNotExist)
        fun userDoesNotExist() = ScoringStateException(ScoringStateError.UserDoesNotExist)
        fun flavourDoesNotExist() = ScoringStateException(ScoringStateError.FlavourDoesNotExist)
    }
}

sealed interface ScoringStateError {
    data object MediaDoesNotExist : ScoringStateError
    data object UserDoesNotExist : ScoringStateError
    data object FlavourDoesNotExist : ScoringStateError
}