package net.moviepumpkins.core.feature.media

import jakarta.persistence.DiscriminatorValue
import jakarta.persistence.Entity

@Entity
@DiscriminatorValue(MediaFormat.Constants.FEATURE_FILM)
class FeatureFilm(
    id: Long? = null,
    title: String,
    plot: String?,
    releaseYear: Int?,
    mpaRating: MPARating?,
    awardWinCount: Int?,
    awardNominationCount: Int?,
    directors: List<String>?,
    writers: List<String>?,
    actors: List<String>?,
    genres: List<String>?,
    posterUrl: String?,
    imdbId: String,
    var runtime: Int?,
) : Media(
    id,
    title,
    plot,
    releaseYear,
    mpaRating,
    awardWinCount,
    awardNominationCount,
    directors,
    writers,
    actors,
    genres,
    posterUrl,
    imdbId,
    format = MediaFormat.FEATURE_FILM,
) {

    constructor(commonDetails: CommonDetails, runtime: Int?) : this(
        title = commonDetails.title,
        plot = commonDetails.plot,
        releaseYear = commonDetails.releaseYear,
        mpaRating = commonDetails.mpaRating,
        awardWinCount = commonDetails.awardWinCount,
        awardNominationCount = commonDetails.awardNominationCount,
        directors = commonDetails.directors,
        writers = commonDetails.writers,
        actors = commonDetails.actors,
        genres = commonDetails.genres,
        posterUrl = commonDetails.posterUrl,
        imdbId = commonDetails.imdbId,
        runtime = runtime,
    )

}
