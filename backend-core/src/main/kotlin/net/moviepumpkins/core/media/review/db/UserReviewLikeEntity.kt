package net.moviepumpkins.core.media.review.db

import jakarta.persistence.*
import net.moviepumpkins.core.user.db.UserAccountEntity

class UserReviewLikeEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    var id: Int?,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_review_id")
    var review: UserReviewEntity,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    var voter: UserAccountEntity,

    var isLike: Boolean
)