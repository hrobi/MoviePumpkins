package net.moviepumpkins.core.media.mediadetails.db

import jakarta.persistence.DiscriminatorValue
import jakarta.persistence.Entity

@Entity
@DiscriminatorValue(MEDIA_TYPE_MOVIE)
class MovieDetailsChangeRequestEntity(
    id: Long?,
    media: MediaDetailsEntity,
    title: String?,
    shortDescription: String?,
    directors: List<String>?,
    writers: List<String>?,
    actors: List<String>?,
    releaseYear: Int?,
    originalTitle: String?,
    country: String?,
    awards: List<String>?,
    awardsWinCount: Int?,
    nominationsCount: Int?,
    lengthInMinutes: Int?
) : MediaDetailsChangeRequestEntity(
    id = id,
    media = media,
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
    otherDetails = lengthInMinutes?.let { MediaAdditionalDetails.ForMovie(it) },
    mediaType = MediaType.MOVIE
)