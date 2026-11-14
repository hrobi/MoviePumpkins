package net.moviepumpkins.core.feature.mediareview.likes

import jakarta.persistence.Column
import jakarta.persistence.Embeddable
import java.io.Serializable

@Embeddable
data class ReviewLikeId(
    @field:Column(name = "review_id")
    var reviewId: Long,

    @field:Column(name = "liker_username")
    var likerUsername: String,
) : Serializable
