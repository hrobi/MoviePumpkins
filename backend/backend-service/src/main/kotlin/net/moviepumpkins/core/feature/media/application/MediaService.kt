package net.moviepumpkins.core.feature.media.application

import jakarta.transaction.Transactional
import net.moviepumpkins.core.feature.media.MediaEntity
import net.moviepumpkins.core.feature.media.MediaRepository
import net.moviepumpkins.core.shared.error.ServiceException
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class MediaService(
    private val mediaRepository: MediaRepository,
) {

    @Transactional
    fun getMediaEntity(id: UUID): MediaEntity {
        return mediaRepository.findById(id)
            .orElseThrow {
                ServiceException.Companion.idNotFound(
                    code = "media.get.idNotFound",
                    idValue = id.toString()
                )
            }
    }

}