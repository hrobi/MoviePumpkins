package net.moviepumpkins.core.media.mediadetails

import jakarta.transaction.Transactional
import net.moviepumpkins.core.app.config.AppProperties
import net.moviepumpkins.core.filestorage.DevLocalPosterTransferService
import net.moviepumpkins.core.media.mediadetails.entity.MediaEntity
import net.moviepumpkins.core.media.mediadetails.entity.MediaRepository
import net.moviepumpkins.core.media.mediadetails.entity.MediaType
import net.moviepumpkins.core.media.mediadetails.mapping.toGenericMediaDetails
import net.moviepumpkins.core.media.mediadetails.model.Media
import org.springframework.stereotype.Component

@Component
class MediaDetailsService(
    private val mediaRepository: MediaRepository,
    private val posterTransferService: DevLocalPosterTransferService,
    private val appProperties: AppProperties,
) {

    private fun mapMediaEntityToMedia(mediaEntity: MediaEntity): Media {
        return when (mediaEntity.mediaType) {
            MediaType.MOVIE -> Media.Movie(
                genericMediaDetails = mediaEntity.toGenericMediaDetails(),
                lengthInMinutes = mediaEntity.otherDetails?.lengthInMinutes,
                releaseYear = mediaEntity.releaseYear
            )

            MediaType.SERIES -> Media.Series(
                genericMediaDetails = mediaEntity.toGenericMediaDetails(),
                startedInYear = mediaEntity.releaseYear,
                seasons = mediaEntity.otherDetails?.seasonCount,
                endedInYear = mediaEntity.otherDetails?.endYear
            )
        }
    }

    @Transactional
    fun getMediaByIdOrNull(id: Long): Media? {
        return mediaRepository.findById(id)
            .map { mapMediaEntityToMedia(it) }
            .orElse(null)
    }

    fun getPosterOrNull(mediaId: Long): ByteArray? {
        val posterFileName = mediaRepository.findPosterFileById(mediaId) ?: return null
        return posterTransferService.getImageAsByteArrayOrNull(posterFileName)
    }

    fun getPosterLink(mediaId: Long): String {
        return "${appProperties.serverUrlBase}/media/${mediaId}/poster"
    }
}