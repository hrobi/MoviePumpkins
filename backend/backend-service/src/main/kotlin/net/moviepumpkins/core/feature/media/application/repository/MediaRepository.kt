package net.moviepumpkins.core.feature.media.application.repository

import net.moviepumpkins.core.feature.media.domain.model.entity.MediaEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface MediaRepository : JpaRepository<MediaEntity, UUID> {
    override fun findById(id: UUID): Optional<MediaEntity>

    fun findAllByImdbIdIn(imdbIds: Collection<String>): List<MediaEntity>
}