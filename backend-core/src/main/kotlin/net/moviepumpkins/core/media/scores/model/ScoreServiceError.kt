package net.moviepumpkins.core.media.scores.model

sealed interface ErrorSavingMediaScore

data object MediaDoesNotExistError : ErrorSavingMediaScore
data class InvalidScoreError(val min: Float, val max: Float) : ErrorSavingMediaScore
data object FlavourDoesNotExistError : ErrorSavingMediaScore