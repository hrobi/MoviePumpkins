package net.moviepumpkins.core.media.review.db

import jakarta.persistence.*
import net.jcip.annotations.Immutable

@Entity
@Immutable
class UserReviewLikingView(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "review_id", updatable = false, insertable = false)
    var reviewId: Int?,

    @OneToOne
    @JoinColumn(name = "user_review_id")
    val review: UserReviewEntity,

    val likes: Int,

    val dislikes: Int
)