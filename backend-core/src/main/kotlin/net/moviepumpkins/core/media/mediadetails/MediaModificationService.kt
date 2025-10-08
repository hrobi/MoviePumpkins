package net.moviepumpkins.core.media.mediadetails

import io.konform.validation.ValidationResult
import jakarta.persistence.EntityManager
import jakarta.transaction.Transactional
import net.moviepumpkins.core.filestorage.DevLocalPosterTransferService
import net.moviepumpkins.core.media.mediadetails.entity.MediaEntity
import net.moviepumpkins.core.media.mediadetails.entity.MediaModificationRepository
import net.moviepumpkins.core.media.mediadetails.entity.MediaRepository
import net.moviepumpkins.core.media.mediadetails.entity.MediaType
import net.moviepumpkins.core.media.mediadetails.mapping.toMediaModificationEntity
import net.moviepumpkins.core.media.mediadetails.mapping.toMediaTypeSpecificDetails
import net.moviepumpkins.core.media.mediadetails.model.MediaModification
import net.moviepumpkins.core.media.mediadetails.model.MovieModification
import net.moviepumpkins.core.media.mediadetails.model.SeriesModification
import net.moviepumpkins.core.media.mediadetails.validation.movieModificationValidation
import net.moviepumpkins.core.media.mediadetails.validation.seriesModificationValidation
import net.moviepumpkins.core.user.entity.UserAccountEntity
import net.moviepumpkins.core.util.Failure
import net.moviepumpkins.core.util.Result
import net.moviepumpkins.core.util.Success
import net.moviepumpkins.core.util.succeedOrElse
import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartFile

sealed interface ErrorCreatingMediaModification
sealed interface ErrorUpdatingMediaModification

sealed interface MediaModificationError {
    data class ViolatedConstraints(val validationResult: ValidationResult<MediaModification>) :
        MediaModificationError, ErrorCreatingMediaModification, ErrorUpdatingMediaModification

    data object MediaDoesNotExist : MediaModificationError, ErrorCreatingMediaModification
    data object ModificationDoesNotExist : MediaModificationError, ErrorUpdatingMediaModification
    data object ModificationAlreadyExists : MediaModificationError, ErrorCreatingMediaModification
    data object PosterImage : MediaModificationError, ErrorCreatingMediaModification, ErrorUpdatingMediaModification
    data object UserDoesNotMatch : MediaModificationError, ErrorUpdatingMediaModification
}

@Component
class MediaModificationService(
    private val mediaRepository: MediaRepository,
    private val posterTransferService: DevLocalPosterTransferService,
    private val mediaModificationRepository: MediaModificationRepository,
    private val entityManager: EntityManager,
) {
    private fun validateMediaModification(mediaModification: MediaModification): Result<Unit, MediaModificationError.ViolatedConstraints> {
        val validation = when (mediaModification) {
            is MovieModification -> movieModificationValidation.validate(mediaModification)
            is SeriesModification -> seriesModificationValidation.validate(mediaModification)
        }
        if (!validation.isValid) {
            return Failure(MediaModificationError.ViolatedConstraints(validation))
        }
        return Success(Unit)
    }

    @Transactional
    fun createModification(
        mediaModification: MediaModification,
        mediaId: Long,
        posterFile: MultipartFile? = null,
    ): Result<Long, ErrorCreatingMediaModification> {
        validateMediaModification(mediaModification).succeedOrElse { return it }

        if (!mediaRepository.existsById(mediaId)) {
            return Failure(MediaModificationError.MediaDoesNotExist)
        }

        val userReference = entityManager.getReference(UserAccountEntity::class.java, mediaModification.username)
        val mediaReference = entityManager.getReference(MediaEntity::class.java, mediaId)

        if (mediaModificationRepository.existsByMediaAndUser(mediaReference, userReference)) {
            return Failure(MediaModificationError.ModificationAlreadyExists)
        }


        val posterFilePath = if (posterFile != null) {
            posterTransferService.upload(posterFile)
                .succeedOrElse { return Failure(MediaModificationError.PosterImage) }
        } else {
            null
        }

        val newMediaModificationEntity = mediaModification.toMediaModificationEntity(
            mediaEntity = mediaReference,
            userAccountEntity = userReference,
            posterFile = posterFilePath
        )

        val id = mediaModificationRepository.save(newMediaModificationEntity).id!!

        return Success(id)
    }

    @Transactional
    fun updateModification(
        mediaModificationId: Long,
        mediaModification: MediaModification,
        posterFile: MultipartFile? = null,
    ): Result<Unit, ErrorUpdatingMediaModification> {
        val mediaModificationEntity = mediaModificationRepository.findById(mediaModificationId).orElse(null)
            ?: return Failure(MediaModificationError.ModificationDoesNotExist)
        validateMediaModification(mediaModification).succeedOrElse { return it }
        if (mediaModificationEntity.user.username != mediaModification.username) {
            return Failure(MediaModificationError.UserDoesNotMatch)
        }

        val posterFilePath = if (posterFile != null) {
            posterTransferService.upload(posterFile)
                .succeedOrElse { return Failure(MediaModificationError.PosterImage) }
        } else {
            null
        }

        if (posterFilePath != null && mediaModificationEntity.posterFile != null) {
            posterTransferService.delete(mediaModificationEntity.posterFile!!)
        }

        mediaModificationEntity.title = mediaModification.title
        mediaModificationEntity.shortDescription = mediaModification.description
        mediaModificationEntity.releaseYear = when (mediaModification) {
            is MovieModification -> mediaModification.releaseYear
            is SeriesModification -> mediaModification.startedInYear
        }
        mediaModificationEntity.directors = mediaModification.directors
        mediaModificationEntity.writers = mediaModification.writers
        mediaModificationEntity.actors = mediaModification.actors
        mediaModificationEntity.originalTitle = mediaModification.originalTitle
        mediaModificationEntity.countries = mediaModification.countries
        mediaModificationEntity.awards = mediaModification.awards
        mediaModificationEntity.awardsWinCount = mediaModification.totalWins
        mediaModificationEntity.nominationsCount = mediaModification.totalNominations
        mediaModificationEntity.posterFile = posterFilePath
        mediaModificationEntity.otherDetails = mediaModification.toMediaTypeSpecificDetails()
        mediaModificationEntity.mediaType = when (mediaModification) {
            is MovieModification -> MediaType.MOVIE
            is SeriesModification -> MediaType.SERIES
        }

        return Success(Unit)
    }

    @Transactional
    fun removeModification(id: Long) {
        val mediaModificationEntity = mediaModificationRepository.findById(id).orElse(null) ?: return
        mediaModificationEntity.posterFile?.let { posterTransferService.delete(it) }
        mediaModificationRepository.delete(mediaModificationEntity)
    }
}