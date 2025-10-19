package net.moviepumpkins.core.media.mediadetails.service

import jakarta.transaction.Transactional
import net.moviepumpkins.core.app.model.UserAccount
import net.moviepumpkins.core.app.model.UserRole
import net.moviepumpkins.core.filestorage.DevLocalPosterTransferService
import net.moviepumpkins.core.media.mediadetails.entity.MediaModificationRepository
import net.moviepumpkins.core.media.mediadetails.entity.MediaRepository
import net.moviepumpkins.core.media.mediadetails.entity.MediaType
import net.moviepumpkins.core.media.mediadetails.exception.MediaModificationStateException
import net.moviepumpkins.core.media.mediadetails.mapping.toMediaModificationEntity
import net.moviepumpkins.core.media.mediadetails.mapping.toMediaTypeSpecificDetails
import net.moviepumpkins.core.media.mediadetails.model.ErrorCreatingMediaModification
import net.moviepumpkins.core.media.mediadetails.model.ErrorRemovingMediaModification
import net.moviepumpkins.core.media.mediadetails.model.ErrorUpdatingMediaModification
import net.moviepumpkins.core.media.mediadetails.model.InvalidMediaModificationError
import net.moviepumpkins.core.media.mediadetails.model.MediaDoesNotExistError
import net.moviepumpkins.core.media.mediadetails.model.MediaModification
import net.moviepumpkins.core.media.mediadetails.model.ModificationAlreadyExistsError
import net.moviepumpkins.core.media.mediadetails.model.ModificationDoesNotExistError
import net.moviepumpkins.core.media.mediadetails.model.MovieModification
import net.moviepumpkins.core.media.mediadetails.model.PosterImageError
import net.moviepumpkins.core.media.mediadetails.model.SeriesModification
import net.moviepumpkins.core.media.mediadetails.model.UserDoesNotMatchError
import net.moviepumpkins.core.media.mediadetails.validation.ValidateMediaModificationService
import net.moviepumpkins.core.user.repository.UserAccountRepository
import net.moviepumpkins.core.util.result.Failure
import net.moviepumpkins.core.util.result.Result
import net.moviepumpkins.core.util.result.Success
import net.moviepumpkins.core.util.result.succeedOrElse
import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartFile

@Component
class MediaModificationService(
    private val mediaRepository: MediaRepository,
    private val posterTransferService: DevLocalPosterTransferService,
    private val mediaModificationRepository: MediaModificationRepository,
    private val validateMediaModificationService: ValidateMediaModificationService,
    private val userAccountRepository: UserAccountRepository,
) {

    @Transactional
    fun createModification(
        mediaModification: MediaModification,
        mediaId: Long,
        posterFile: MultipartFile? = null,
    ): Result<Long, ErrorCreatingMediaModification> {
        validateMediaModificationService.tryValidate(mediaModification)
            .succeedOrElse { return mapFailure(::InvalidMediaModificationError) }

        if (!mediaRepository.existsById(mediaId)) {
            return Failure(MediaDoesNotExistError)
        }

        val userReference = userAccountRepository.getReferenceById(mediaModification.username)
        val mediaReference = mediaRepository.getReferenceById(mediaId)

        if (mediaModificationRepository.existsByMediaAndUser(mediaReference, userReference)) {
            return Failure(ModificationAlreadyExistsError)
        }


        val posterFilePath = if (posterFile != null) {
            posterTransferService.upload(posterFile)
                .succeedOrElse { return mapFailure { PosterImageError } }
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
            ?: return Failure(ModificationDoesNotExistError)
        validateMediaModificationService.tryValidate(mediaModification)
            .succeedOrElse { return mapFailure(::InvalidMediaModificationError) }

        if (mediaModificationEntity.user.username != mediaModification.username) {
            return Failure(UserDoesNotMatchError)
        }

        val posterFilePath = if (posterFile != null) {
            posterTransferService.upload(posterFile)
                .succeedOrElse { return Failure(PosterImageError) }
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
    fun removeModification(id: Long, remover: UserAccount): Result<Unit, ErrorRemovingMediaModification> {
        val mediaModificationEntity = mediaModificationRepository.findById(id).orElse(null)
            ?: return Failure(ModificationDoesNotExistError)
        if (mediaModificationEntity.user.username != remover.username && remover.role != UserRole.ADMIN) {
            return Failure(UserDoesNotMatchError)
        }
        mediaModificationEntity.posterFile?.let { posterTransferService.delete(it) }
        mediaModificationRepository.delete(mediaModificationEntity)
        return Success(Unit)
    }

    @Transactional
    fun removeModification(modificationId: Long) {
        val mediaModificationEntity = mediaModificationRepository.findByIdAndFetchMedia(modificationId)
            ?: throw MediaModificationStateException.mediaDoesNotExist()
        mediaModificationRepository.delete(mediaModificationEntity)
    }

    @Transactional
    fun mergeModification(modificationId: Long) {
        val mediaModificationEntity = mediaModificationRepository.findByIdAndFetchMedia(modificationId)
            ?: throw MediaModificationStateException.mediaDoesNotExist()
        val mediaEntity = mediaModificationEntity.media
        MediaModificationMerger(mediaEntity, mediaModificationEntity).merge()
    }
}