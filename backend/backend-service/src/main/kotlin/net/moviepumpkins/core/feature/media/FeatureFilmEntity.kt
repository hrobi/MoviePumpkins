package net.moviepumpkins.core.feature.media

import jakarta.persistence.DiscriminatorValue
import jakarta.persistence.Entity
import java.util.UUID

@Entity
@DiscriminatorValue(MediaFormat.Constants.FEATURE_FILM)
class FeatureFilmEntity(
    id: UUID? = null,
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
) : MediaEntity(
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

    constructor(commonData: CommonData, runtime: Int?) : this(
        title = commonData.title,
        plot = commonData.plot,
        releaseYear = commonData.releaseYear,
        mpaRating = commonData.mpaRating,
        awardWinCount = commonData.awardWinCount,
        awardNominationCount = commonData.awardNominationCount,
        directors = commonData.directors,
        writers = commonData.writers,
        actors = commonData.actors,
        genres = commonData.genres,
        posterUrl = commonData.posterUrl,
        imdbId = commonData.imdbId,
        runtime = runtime,
    )

}