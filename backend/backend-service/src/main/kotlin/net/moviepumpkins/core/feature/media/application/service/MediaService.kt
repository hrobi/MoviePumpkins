package net.moviepumpkins.core.feature.media.application.service

import jakarta.transaction.Transactional
import net.moviepumpkins.core.feature.media.application.repository.MediaRepository
import net.moviepumpkins.core.feature.media.domain.model.entity.MediaEntity
import net.moviepumpkins.core.shared.service.exception.ServiceException
import org.springframework.stereotype.Component
import java.util.*

@Component
class MediaService(
    private val mediaRepository: MediaRepository,
) {

    @Transactional
    fun getMediaEntity(id: UUID): MediaEntity {
        return mediaRepository.findById(id)
            .orElseThrow { ServiceException.idNotFound(code = "media.get.idNotFound", idValue = id.toString()) }
    }

}