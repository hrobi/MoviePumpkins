package net.moviepumpkins.core.media.mediadetails.entity

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import net.moviepumpkins.core.app.entity.BaseEntity
import org.springframework.data.jpa.repository.JpaRepository

interface MediaModificationPosterRepository : JpaRepository<MediaModificationPosterEntity, Long> {
    fun findByMediaIdAndUsername(mediaId: Long, username: String): MediaModificationPosterEntity?
}

@Entity
@Table(name = "media_modification_poster")
class MediaModificationPosterEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long? = null,

    val mediaId: Long,

    val username: String,

    val posterFile: String,
) : BaseEntity()