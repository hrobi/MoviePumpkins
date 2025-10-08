package net.moviepumpkins.core.media.review.entity

import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import net.moviepumpkins.core.user.entity.UserAccountEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface ReviewLikeRepository : JpaRepository<ReviewLikeEntity, Long> {
    @Query("SELECT rle.id FROM ReviewLikeEntity rle WHERE rle.review = :review AND rle.rater = :rater")
    fun getIdByReviewAndRater(review: ReviewEntity, rater: UserAccountEntity): Long?
}

@Entity
@Table(name = "review_like")
class ReviewLikeEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    var id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id")
    var review: ReviewEntity,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "username")
    var rater: UserAccountEntity,

    var isLike: Boolean,
)