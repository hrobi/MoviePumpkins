package net.moviepumpkins.core.media.scores.db

import jakarta.persistence.*
import net.moviepumpkins.core.general.BaseEntity
import net.moviepumpkins.core.media.mediadetails.db.MediaDetailsEntity
import net.moviepumpkins.core.user.db.UserAccountEntity

@Entity
@Table(name = "user_media_flavour_score")
class UserMediaFlavourScoreEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    var id: Int?,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    var user: UserAccountEntity,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "media_details_id")
    var media: MediaDetailsEntity,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "media_flavour_id")
    var flavour: MediaFlavourReadOnlyEntity,

    var score: Int
) : BaseEntity()