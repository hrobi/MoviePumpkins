package net.moviepumpkins.core

import net.moviepumpkins.core.media.mediadetails.entity.MediaRepository
import org.springframework.stereotype.Component

@Component
class TestHelperService(
    private val mediaRepository: MediaRepository,
) {
    fun getDuneId(): Long {
        return mediaRepository.getIdByTitleOrNull("Dune: Part One")!!
    }

    fun getFlowId(): Long {
        return mediaRepository.getIdByTitleOrNull("Flow")!!
    }
}