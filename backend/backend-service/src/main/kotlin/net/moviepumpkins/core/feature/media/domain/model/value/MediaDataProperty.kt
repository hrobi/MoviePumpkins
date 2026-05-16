package net.moviepumpkins.core.feature.media.domain.model.value

sealed interface MediaDataProperty {
    object Title : MediaDataProperty
    object Plot : MediaDataProperty
    object ReleaseYear : MediaDataProperty
    object MpaRating : MediaDataProperty
    object AwardsCount : MediaDataProperty
    object Directors : MediaDataProperty
    object Writers : MediaDataProperty
    object Actors : MediaDataProperty
    object Genres : MediaDataProperty
    data class Format(val formatProperty: MediaFormatDataProperty)
}
