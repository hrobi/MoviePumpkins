package net.moviepumpkins.core.feature.media.domain.model.value

sealed interface MediaFormatDataProperty {
    enum class FeatureFilm : MediaFormatDataProperty {
        Runtime
    }

    enum class Series : MediaFormatDataProperty {
        NumberOfSeasons,
        LastYear
    }
}