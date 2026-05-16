package net.moviepumpkins.core.feature.media.application.controller

import net.moviepumpkins.core.api.controllers.MediaController
import net.moviepumpkins.core.api.models.MediaDto
import net.moviepumpkins.core.feature.media.application.mapper.MediaMapper
import net.moviepumpkins.core.feature.media.application.service.MediaService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
class MediaOverviewController(
    private val mediaService: MediaService,
) : MediaController {

    override fun getById(id: UUID): ResponseEntity<MediaDto> {
        return ResponseEntity.ok(MediaMapper.mapEntityToDto(mediaService.getMediaEntity(id)))
    }

}