package net.moviepumpkins.core.media.review.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import org.hibernate.annotations.Immutable

@Entity
@Immutable
@Table(name = "review_likes_aggregate_view")
class ReviewLikesAggregateView(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "review_id", updatable = false, insertable = false)
    var reviewId: Long?,

    @OneToOne
    @JoinColumn(name = "review_id")
    val review: ReviewEntity,

    val likes: Int,

    val dislikes: Int
)