package net.moviepumpkins.core.feature.media

import jakarta.persistence.Column
import jakarta.persistence.DiscriminatorValue
import jakarta.persistence.Entity

@Entity
@DiscriminatorValue(MediaFormat.Constants.SERIES)
class Series(
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
    var lastYear: Int?,
    @Column(name = "number_of_seasons")
    var seasons: Int?,
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
    format = MediaFormat.SERIES
) {

    constructor(commonDetails: CommonDetails, lastYear: Int?, seasons: Int?) : this(
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
        lastYear = lastYear,
        seasons = seasons,
    )

}
