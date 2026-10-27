package net.moviepumpkins.core.feature.media

import jakarta.persistence.Column
import jakarta.persistence.DiscriminatorValue
import jakarta.persistence.Entity

@Entity
@DiscriminatorValue(MediaFormat.Constants.SERIES)
class SeriesEntity(
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
    format = MediaFormat.SERIES
) {

    constructor(commonData: CommonData, lastYear: Int?, seasons: Int?) : this(
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
        lastYear = lastYear,
        seasons = seasons,
    )

}
