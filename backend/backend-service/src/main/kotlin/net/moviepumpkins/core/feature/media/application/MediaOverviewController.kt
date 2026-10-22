package net.moviepumpkins.core.feature.media.application

import net.moviepumpkins.core.api.controllers.MediaController
import net.moviepumpkins.core.api.models.MediaDto
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
class MediaOverviewController(
    private val mediaOverviewService: MediaOverviewService,
) : MediaController {

    override fun getById(id: UUID): ResponseEntity<MediaDto> {
        return ResponseEntity.ok(mediaOverviewService.getMediaById(id))
    }

}