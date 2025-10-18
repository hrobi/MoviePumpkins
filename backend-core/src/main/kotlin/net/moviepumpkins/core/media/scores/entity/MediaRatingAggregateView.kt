package net.moviepumpkins.core.media.scores.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.IdClass
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import org.hibernate.annotations.Immutable
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.io.Serializable

interface MediaRatingAggregateRepository : JpaRepository<MediaRatingAggregateView, MediaRatingAggregateId> {
    @Query("SELECT mrav FROM MediaRatingAggregateView mrav JOIN FETCH mrav.flavour WHERE mrav.mediaId = :mediaId")
    fun findByMediaId(mediaId: Long, pageable: Pageable): List<MediaRatingAggregateView>

    fun countByMediaId(mediaId: Long): Int?
}

class MediaRatingAggregateId(val mediaId: Long, val mediaFlavourId: String) : Serializable {
    constructor() : this(0, "")
}

@Immutable
@Entity
@Table(name = "media_rating_aggregate_view")
@IdClass(MediaRatingAggregateId::class)
class MediaRatingAggregateView(
    @Id
    @Column(updatable = false, insertable = false)
    val mediaId: Long,

    @Id
    @Column(name = "media_flavour_id", updatable = false, insertable = false)
    val mediaFlavourId: String,

    @OneToOne
    @JoinColumn(name = "media_flavour_id")
    val flavour: MediaFlavourReadOnlyEntity,

    val averageScore: Float,
    val raterCount: Int,
)