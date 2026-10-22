package net.moviepumpkins.core.feature.media

import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional
import java.util.UUID

interface MediaRepository : JpaRepository<MediaEntity, UUID> {
    override fun findById(id: UUID): Optional<MediaEntity>

    fun findAllByImdbIdIn(imdbIds: Collection<String>): List<MediaEntity>
}