package net.moviepumpkins.core.media.mediadetails.db

enum class MediaType {
    MOVIE,
    SERIES
}

const val MEDIA_TYPE_MOVIE = "MOVIE"
const val MEDIA_TYPE_SERIES = "SERIES"


sealed interface MediaAdditionalDetails {
    data class ForMovie(val lengthInMinutes: Int) : MediaAdditionalDetails
    data class ForSeries(val seasonCount: Int?, val episodeCount: Int?, val endYear: Int?) :
        MediaAdditionalDetails
}