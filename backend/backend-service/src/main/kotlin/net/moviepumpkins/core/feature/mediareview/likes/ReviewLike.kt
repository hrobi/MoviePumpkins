package net.moviepumpkins.core.feature.mediareview.likes

import jakarta.persistence.EmbeddedId
import jakarta.persistence.Entity
import jakarta.persistence.Table

@Entity
@Table(name = "review_like")
class ReviewLike(

    @field:EmbeddedId
    var id: ReviewLikeId

)
