package net.moviepumpkins.core.media.mediadetails.mapping

import net.moviepumpkins.core.media.mediadetails.entity.MediaEntity
import net.moviepumpkins.core.media.mediadetails.model.GenericMediaDetails

fun MediaEntity.toGenericMediaDetails() = GenericMediaDetails(
    id!!,
    title = title,
    shortDescription = shortDescription,
    directors = directors,
    writers = writers,
    actors = actors,
    originalTitle = originalTitle,
    countries = countries,
    awards = awards,
    awardsWinCount = awardsWinCount,
    nominationsCount = nominationsCount
)