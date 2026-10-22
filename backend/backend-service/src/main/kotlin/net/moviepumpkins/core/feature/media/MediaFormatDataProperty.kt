package net.moviepumpkins.core.feature.media

sealed interface MediaFormatDataProperty {
    enum class FeatureFilm : MediaFormatDataProperty {
        Runtime
    }

    enum class Series : MediaFormatDataProperty {
        NumberOfSeasons,
        LastYear
    }
}