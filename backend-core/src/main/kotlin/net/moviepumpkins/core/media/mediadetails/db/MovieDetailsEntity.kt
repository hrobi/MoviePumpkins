package net.moviepumpkins.core.media.mediadetails.db

import jakarta.persistence.DiscriminatorValue
import jakarta.persistence.Entity

@Entity
@DiscriminatorValue(MEDIA_TYPE_MOVIE)
class MovieDetailsEntity(
    id: Long?,
    title: String,
    shortDescription: String,
    directors: List<String>,
    writers: List<String>,
    actors: List<String>,
    releaseYear: Int,
    originalTitle: String?,
    country: String?,
    awards: List<String>,
    awardsWinCount: Int?,
    nominationsCount: Int?,
    lengthInMinutes: Int,
) : MediaDetailsEntity(
    id = id,
    title = title,
    shortDescription = shortDescription,
    directors = directors,
    writers = writers,
    actors = actors,
    releaseYear = releaseYear,
    originalTitle = originalTitle,
    country = country,
    awards = awards,
    awardsWinCount = awardsWinCount,
    nominationsCount = nominationsCount,
    otherDetails = MediaAdditionalDetails.ForMovie(lengthInMinutes),
    mediaType = MediaType.MOVIE
)