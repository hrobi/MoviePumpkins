package net.moviepumpkins.core.media.mediadetails

import net.moviepumpkins.core.app.config.AppProperties
import net.moviepumpkins.core.app.config.AuthenticationFacade
import net.moviepumpkins.core.app.exception.ConflictException
import net.moviepumpkins.core.app.exception.ForbiddenException
import net.moviepumpkins.core.app.exception.NotFoundException
import net.moviepumpkins.core.app.exception.buildBadRequestException
import net.moviepumpkins.core.app.mapping.toBadRequestException
import net.moviepumpkins.core.integration.controllers.MediaController
import net.moviepumpkins.core.integration.controllers.MediaPosterController
import net.moviepumpkins.core.integration.models.CreateMediaModificationRequest
import net.moviepumpkins.core.integration.models.GetMediaResponse
import net.moviepumpkins.core.integration.models.UpdateMediaModificationRequest
import net.moviepumpkins.core.media.mediadetails.mapping.toGetMediaResponse
import net.moviepumpkins.core.media.mediadetails.mapping.toMediaModification
import net.moviepumpkins.core.media.mediadetails.model.InvalidMediaModificationError
import net.moviepumpkins.core.media.mediadetails.model.MediaDoesNotExistError
import net.moviepumpkins.core.media.mediadetails.model.ModificationAlreadyExistsError
import net.moviepumpkins.core.media.mediadetails.model.ModificationDoesNotExistError
import net.moviepumpkins.core.media.mediadetails.model.PosterImageError
import net.moviepumpkins.core.media.mediadetails.model.UserDoesNotMatchError
import net.moviepumpkins.core.media.mediadetails.service.MediaModificationService
import net.moviepumpkins.core.util.result.succeedOrElse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import java.net.URI

@RestController
class MediaController(
    private val mediaDetailsService: MediaDetailsService,
    private val mediaModificationService: MediaModificationService,
    private val authenticationFacade: AuthenticationFacade,
    private val appProperties: AppProperties,
) : MediaController, MediaPosterController {

    override fun getMediaById(id: Long): ResponseEntity<GetMediaResponse> {
        return mediaDetailsService
            .getMediaByIdOrNull(id)
            ?.toGetMediaResponse(withPosterLink = mediaDetailsService.getPosterLink(id))
            ?.let { ResponseEntity.ok(it) }
            ?: throw NotFoundException()
    }

    override fun getMediaPosterById(id: Long): ResponseEntity<ByteArray> {
        val posterByteArray = mediaDetailsService.getPosterOrNull(id) ?: throw NotFoundException()
        return ResponseEntity.ok(posterByteArray)
    }

    private val badPosterException = buildBadRequestException {
        forField(
            "poster",
            "poster file needs to be a legitimate image file at most ${appProperties.posterImage.maxSizeInBytes} bytes"
        )
    }

    @RequestMapping(
        value = ["/media-modifications/"],
        produces = [],
        method = [RequestMethod.POST],
        consumes = ["multipart/form-data"],
    )
    fun createMediaModification(
        @RequestPart(name = "details", required = true) createMediaModificationRequest: CreateMediaModificationRequest,
        @RequestPart(name = "poster") posterFile: MultipartFile?,
    ): ResponseEntity<Unit> {
        val username = authenticationFacade.authenticationName
        val mediaId = mediaModificationService.createModification(
            mediaModification = createMediaModificationRequest.toMediaModification(username),
            posterFile = posterFile,
            mediaId = createMediaModificationRequest.mediaId
        ).succeedOrElse {
            when (val error = cause) {
                MediaDoesNotExistError -> throw NotFoundException()
                ModificationAlreadyExistsError -> throw ConflictException()
                is InvalidMediaModificationError -> throw error.validationResult.errors.toBadRequestException(
                    pathPrefix = "details."
                )

                PosterImageError -> badPosterException
            }
        }

        return ResponseEntity.created(
            URI.create("${appProperties.serverUrlBase}/media-modifications/${mediaId}")
        ).build()
    }

    @RequestMapping(
        value = ["/media-modifications/{id}"],
        produces = [],
        method = [RequestMethod.PUT],
        consumes = ["multipart/form-data"],
    )
    fun updateMediaModification(
        @RequestPart(name = "details", required = true) updateMediaModificationRequest: UpdateMediaModificationRequest,
        @RequestPart(name = "poster") posterFile: MultipartFile?,
        @PathVariable(value = "id", required = true) id: Long,
    ): ResponseEntity<Unit> {
        mediaModificationService.updateModification(
            mediaModificationId = id,
            mediaModification = updateMediaModificationRequest.toMediaModification(username = authenticationFacade.authenticationName),
            posterFile = posterFile
        ).succeedOrElse {
            when (val error = cause) {
                ModificationDoesNotExistError -> throw NotFoundException()
                PosterImageError -> throw badPosterException
                UserDoesNotMatchError -> throw ForbiddenException()
                is InvalidMediaModificationError -> throw error.validationResult.errors.toBadRequestException(
                    pathPrefix = "details."
                )
            }
        }

        return ResponseEntity.noContent().build()
    }

    @RequestMapping(
        value = ["/media-modifications/{id}"],
        produces = [],
        method = [RequestMethod.DELETE],
    )
    fun deleteMediaModification(@PathVariable(value = "id", required = true) id: Long): ResponseEntity<Unit> {
        val userAccount = authenticationFacade.extractUserAccount()
        mediaModificationService.removeModification(id, userAccount).succeedOrElse {
            when (cause) {
                ModificationDoesNotExistError -> throw NotFoundException()
                UserDoesNotMatchError -> throw ForbiddenException()
            }
        }
        return ResponseEntity.noContent().build()
    }
}