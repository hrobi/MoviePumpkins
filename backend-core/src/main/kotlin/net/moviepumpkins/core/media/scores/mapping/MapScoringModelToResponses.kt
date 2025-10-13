package net.moviepumpkins.core.media.scores.mapping

import net.moviepumpkins.core.flavour.model.MediaFlavour
import net.moviepumpkins.core.integration.models.GetFlavourResponse
import net.moviepumpkins.core.integration.models.GetMediaScoreResponse
import net.moviepumpkins.core.media.scores.model.MediaScore

fun MediaFlavour.toGetFlavourResponse() = GetFlavourResponse(
    id = id,
    name = name,
    description = description
)

fun MediaScore.toGetMediaScoreResponse() = GetMediaScoreResponse(
    score = score,
    flavour = flavour.toGetFlavourResponse(),
    count = count
)