package net.moviepumpkins.core.application.fakedata.disposablereviewlikes

import jakarta.persistence.EmbeddedId
import jakarta.persistence.Entity
import jakarta.persistence.Table
import net.moviepumpkins.core.feature.mediareview.likes.ReviewLikeId
import org.hibernate.annotations.SQLRestriction

@Entity
@Table(name = "review_like")
@SQLRestriction("disposable_fake = true")
class DisposableReviewLike(

    @field:EmbeddedId
    var id: ReviewLikeId

) {
    val disposableFake: Boolean = true
}
