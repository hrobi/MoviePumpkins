package net.moviepumpkins.core.media

import net.moviepumpkins.core.app.config.AppProperties
import net.moviepumpkins.core.app.config.AuthenticationFacade
import net.moviepumpkins.core.app.exception.BadRequestException
import net.moviepumpkins.core.app.exception.ConflictException
import net.moviepumpkins.core.app.exception.ForbiddenException
import net.moviepumpkins.core.app.exception.NotFoundException
import net.moviepumpkins.core.app.mapping.toBadRequestException
import net.moviepumpkins.core.integration.controllers.MediaController
import net.moviepumpkins.core.integration.controllers.MediaPosterController
import net.moviepumpkins.core.integration.models.BadRequestBodyError
import net.moviepumpkins.core.integration.models.CreateMediaModificationRequest
import net.moviepumpkins.core.integration.models.GetMediaResponse
import net.moviepumpkins.core.integration.models.Status400Response
import net.moviepumpkins.core.integration.models.UpdateMediaModificationRequest
import net.moviepumpkins.core.media.mediadetails.MediaDetailsService
import net.moviepumpkins.core.media.mediadetails.MediaModificationError
import net.moviepumpkins.core.media.mediadetails.MediaModificationService
import net.moviepumpkins.core.media.mediadetails.mapping.toGetMediaResponse
import net.moviepumpkins.core.media.mediadetails.mapping.toMediaModification
import net.moviepumpkins.core.util.succeedOrElse
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
            when (val error = it.cause) {
                MediaModificationError.MediaDoesNotExist -> throw NotFoundException()
                MediaModificationError.ModificationAlreadyExists -> throw ConflictException()
                is MediaModificationError.ViolatedConstraints -> throw error.validationResult.errors.toBadRequestException(
                    pathPrefix = "details."
                )

                MediaModificationError.PosterImage -> throw BadRequestException(
                    Status400Response(
                        listOf(
                            BadRequestBodyError(
                                fields = listOf("poster"),
                                reason = "poster file needs to be a legitimate image file at most ${appProperties.posterImage.maxSizeInBytes} bytes"
                            )
                        )
                    )
                )
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
            when (val error = it.cause) {
                MediaModificationError.ModificationDoesNotExist -> throw NotFoundException()
                MediaModificationError.PosterImage -> throw BadRequestException(
                    Status400Response(
                        listOf(
                            BadRequestBodyError(
                                fields = listOf("poster"),
                                reason = "poster file needs to be a legitimate image file at most ${appProperties.posterImage.maxSizeInBytes} bytes"
                            )
                        )
                    )
                )

                MediaModificationError.UserDoesNotMatch -> throw ForbiddenException()
                is MediaModificationError.ViolatedConstraints -> throw error.validationResult.errors.toBadRequestException(
                    pathPrefix = "details."
                )
            }
        }

        return ResponseEntity.noContent().build()
    }
}