package net.moviepumpkins.core.media.review.entity

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

@Entity
@Table(name = "review")
class ReviewEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    var id: Int?,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "username")
    var user: UserAccountEntity,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "media_id")
    var media: MediaEntity
) : BaseEntity()