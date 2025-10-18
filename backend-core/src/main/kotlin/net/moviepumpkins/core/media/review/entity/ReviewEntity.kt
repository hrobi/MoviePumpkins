package net.moviepumpkins.core.media.review.entity

import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import net.moviepumpkins.core.app.entity.BaseEntity
import net.moviepumpkins.core.media.mediadetails.entity.MediaEntity
import net.moviepumpkins.core.user.entity.UserAccountEntity
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface ReviewRepository : JpaRepository<ReviewEntity, Long> {
    @Query("SELECT re FROM ReviewEntity re JOIN FETCH re.creator LEFT JOIN FETCH re.reviewLikesAggregateView WHERE re.media = :media")
    fun findByMedia(media: MediaEntity, pageable: Pageable): List<ReviewEntity>

    @Query(
        """SELECT re FROM ReviewEntity re 
        JOIN FETCH re.creator 
        LEFT JOIN FETCH re.reviewLikesAggregateView WHERE re.media = :media AND re.creator = :creator"""
    )
    fun findByMediaAndCreator(media: MediaEntity, creator: UserAccountEntity): ReviewEntity?

    @Query(
        """SELECT re FROM ReviewEntity re 
        JOIN FETCH re.creator 
        WHERE re.id = :id"""
    )
    fun findByIdOrNull(id: Long): ReviewEntity?

    @Query("""SELECT re.id FROM ReviewEntity re WHERE re.media = :media AND re.creator = :creator""")
    fun getIdByMediaAndCreatorOrNull(media: MediaEntity, creator: UserAccountEntity): Long?

    fun countByMediaId(mediaId: Long): Int?

}

@Entity
@Table(name = "review")
class ReviewEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    var id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "username")
    var creator: UserAccountEntity,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "media_id")
    var media: MediaEntity,

    var title: String,

    var content: String,

    var spoilerFree: Boolean,

    @OneToOne
    @JoinColumn(name = "id", insertable = false, updatable = false)
    val reviewLikesAggregateView: ReviewLikesAggregateView? = null,
) : BaseEntity()