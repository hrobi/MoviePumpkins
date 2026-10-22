package net.moviepumpkins.core.feature.media.application

import net.moviepumpkins.core.api.controllers.MediaController
import net.moviepumpkins.core.api.models.MediaDto
import net.moviepumpkins.core.feature.media.MediaMapper
import net.moviepumpkins.core.feature.media.application.MediaService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
class MediaOverviewController(
    private val mediaService: MediaService,
) : MediaController {

    override fun getById(id: UUID): ResponseEntity<MediaDto> {
        return ResponseEntity.ok(MediaMapper.mapEntityToDto(mediaService.getMediaEntity(id)))
    }

}