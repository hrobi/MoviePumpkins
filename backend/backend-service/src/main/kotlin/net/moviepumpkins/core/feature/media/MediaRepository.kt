package net.moviepumpkins.core.feature.media

import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

interface MediaRepository : JpaRepository<Media, Long> {
    override fun findById(id: Long): Optional<Media>

    fun findAllByImdbIdIn(imdbIds: Collection<String>): List<Media>
}
