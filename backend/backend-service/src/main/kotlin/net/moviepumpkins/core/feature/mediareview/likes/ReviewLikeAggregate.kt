package net.moviepumpkins.core.feature.mediareview.likes

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import net.moviepumpkins.core.feature.mediareview.Review
import org.hibernate.annotations.Immutable

@Entity
@Table(name = "review_like_aggregate")
@Immutable
class ReviewLikeAggregate(

    @field:Id
    @field:Column(name = "review_id")
    val reviewId: Long,

    @field:Column(name = "like_sum")
    val likeSum: Int,

    @field:OneToOne
    @field:JoinColumn(name = "review_id")
    val review: Review

)
