package net.moviepumpkins.core.feature.media.domain.model.value

sealed interface MediaFormatData {
    data class FeatureFilm(val runtimeInMinutes: Int?) : MediaFormatData
    data class Series(val numberOfSeasons: Int?, val lastYear: Int?) : MediaFormatData
}