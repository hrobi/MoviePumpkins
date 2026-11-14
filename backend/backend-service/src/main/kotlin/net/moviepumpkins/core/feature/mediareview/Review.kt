package net.moviepumpkins.core.feature.mediareview

import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import net.moviepumpkins.core.feature.media.Media
import net.moviepumpkins.core.feature.mediareview.likes.ReviewLikeAggregate
import net.moviepumpkins.core.feature.user.UserAccount
import net.moviepumpkins.core.shared.entity.EntityBase

@Entity
@Table(name = "review")
class Review(

    var title: String,

    var content: String,

    @field:ManyToOne
    @field:JoinColumn(name = "reviewer")
    var reviewer: UserAccount,

    @field:ManyToOne
    @field:JoinColumn(name = "media_id")
    var media: Media,

    @field:OneToOne(mappedBy = "review", fetch = FetchType.LAZY)
    var likeAggregate: ReviewLikeAggregate? = null

) : EntityBase()
