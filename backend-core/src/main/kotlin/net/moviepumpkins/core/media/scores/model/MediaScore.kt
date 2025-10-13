package net.moviepumpkins.core.media.scores.model

import net.moviepumpkins.core.flavour.model.MediaFlavour

data class MediaScore(
    val flavour: MediaFlavour,
    val score: Float,
    val count: Int = 1,
)
