package net.moviepumpkins.core.feature.media

import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

interface MediaRepository : JpaRepository<MediaEntity, Long> {
    override fun findById(id: Long): Optional<MediaEntity>

    fun findAllByImdbIdIn(imdbIds: Collection<String>): List<MediaEntity>
}
