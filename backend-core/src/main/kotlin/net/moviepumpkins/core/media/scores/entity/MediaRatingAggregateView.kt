package net.moviepumpkins.core.media.scores.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.IdClass
import jakarta.persistence.Table
import org.hibernate.annotations.Immutable
import java.io.Serializable

class MediaRatingAggregateId(val mediaId: Long?, val mediaFlavourId: Long?) : Serializable

@Immutable
@Entity
@Table(name = "media_rating_aggregate_view")
@IdClass(MediaRatingAggregateId::class)
class MediaRatingAggregateView(
    @Id
    @Column(updatable = false, insertable = false)
    var mediaId: Long?,

    @Id
    @Column(updatable = false, insertable = false)
    var mediaFlavourId: Long?,

    var score: Double
)