package net.moviepumpkins.core.feature.media.application

import jakarta.transaction.Transactional
import net.moviepumpkins.core.api.models.MediaDto
import net.moviepumpkins.core.feature.media.MediaMapper
import net.moviepumpkins.core.feature.media.MediaRepository
import net.moviepumpkins.core.feature.media.error.MediaNotFoundException
import org.springframework.stereotype.Component

@Component
class MediaOverviewService(
    private val mediaRepository: MediaRepository,
) {

    @Transactional
    fun getMediaById(id: Long): MediaDto {
        return MediaMapper.mapEntityToDto(
            mediaRepository.findById(id)
                .orElseThrow {
                    MediaNotFoundException(id)
                })
    }

}
