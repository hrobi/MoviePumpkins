package net.moviepumpkins.core.application.fakedata.disposablereviews

import jakarta.persistence.Entity
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import net.moviepumpkins.core.application.fakedata.disposableusers.DisposableUserAccount
import net.moviepumpkins.core.feature.media.Media
import net.moviepumpkins.core.shared.entity.EntityBase
import org.hibernate.annotations.SQLRestriction

@Entity
@Table(name = "review")
@SQLRestriction("disposable_fake = true")
class DisposableReview(

    var title: String,

    var content: String,

    @field:ManyToOne
    @field:JoinColumn(name = "reviewer")
    var reviewer: DisposableUserAccount,

    @field:ManyToOne
    @field:JoinColumn(name = "media_id")
    var media: Media

) : EntityBase() {

    val disposableFake = true

}