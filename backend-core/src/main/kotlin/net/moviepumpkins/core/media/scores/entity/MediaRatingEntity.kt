package net.moviepumpkins.core.media.scores.entity

import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import net.moviepumpkins.core.app.entity.BaseEntity
import net.moviepumpkins.core.media.mediadetails.entity.MediaEntity
import net.moviepumpkins.core.user.entity.UserAccountEntity
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface MediaRatingRepository : JpaRepository<MediaRatingEntity, Long> {
    @Query("SELECT mre.id FROM MediaRatingEntity mre WHERE mre.user = :user AND mre.media = :media AND mre.flavour = :flavour")
    fun getIdByUserAndMediaAndFlavourOrNull(
        user: UserAccountEntity,
        media: MediaEntity,
        flavour: MediaFlavourReadOnlyEntity,
    ): Long?

    fun findByUserAndMedia(
        user: UserAccountEntity,
        media: MediaEntity,
        pageable: Pageable,
    ): List<MediaRatingEntity>

    fun findByUserAndMediaAndFlavour(
        user: UserAccountEntity,
        media: MediaEntity,
        flavour: MediaFlavourReadOnlyEntity,
    ): MediaRatingEntity?

    fun countByMediaAndUser(media: MediaEntity, user: UserAccountEntity): Int?
}

@Entity
@Table(name = "media_rating")
class MediaRatingEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    var id: Long?,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "username")
    var user: UserAccountEntity,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "media_id")
    var media: MediaEntity,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "media_flavour_id")
    var flavour: MediaFlavourReadOnlyEntity,

    var score: Float,
) : BaseEntity()