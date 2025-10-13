package net.moviepumpkins.core.media.scores.mapping

import net.moviepumpkins.core.flavour.model.MediaFlavour
import net.moviepumpkins.core.media.scores.entity.MediaFlavourReadOnlyEntity
import net.moviepumpkins.core.media.scores.entity.MediaRatingAggregateView
import net.moviepumpkins.core.media.scores.entity.MediaRatingEntity
import net.moviepumpkins.core.media.scores.model.MediaScore

fun MediaFlavourReadOnlyEntity.toMediaFlavour() = MediaFlavour(
    id = id!!,
    name = flavourName,
    description = shortDescription,
)

fun MediaRatingEntity.toMediaScore() = MediaScore(
    score = score,
    flavour = flavour.toMediaFlavour(),
)

fun MediaRatingAggregateView.toMediaScore() = MediaScore(
    score = averageScore,
    flavour = flavour.toMediaFlavour(),
    count = raterCount
)