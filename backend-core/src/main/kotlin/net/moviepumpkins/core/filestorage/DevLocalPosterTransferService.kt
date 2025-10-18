package net.moviepumpkins.core.filestorage

import net.moviepumpkins.core.app.config.PosterImageProperties
import net.moviepumpkins.core.util.result.FailureMappable
import net.moviepumpkins.core.util.result.Result
import net.moviepumpkins.core.util.result.Success
import org.imgscalr.Scalr
import org.springframework.context.annotation.Profile
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartFile
import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import java.util.*
import javax.imageio.ImageIO

sealed interface ErrorUploadingPoster : FailureMappable<ErrorUploadingPoster> {
    data class TooLargeFile(val maximumSizeInBytes: Long, val actualSizeInBytes: Long) : ErrorUploadingPoster
    data class InvalidContentType(val contentType: String?) : ErrorUploadingPoster
    data object NotAnImage : ErrorUploadingPoster
}

@Component
@Profile("dev")
class DevLocalPosterTransferService(
    private val posterImageProperties: PosterImageProperties,
) {

    private fun getCanonicalFilePathBase() = ClassPathResource("/mock/posters", javaClass).file.canonicalPath


    fun upload(multipartFile: MultipartFile): Result<String, ErrorUploadingPoster> {

        if (multipartFile.size > posterImageProperties.maxSizeInBytes) {
            return ErrorUploadingPoster.TooLargeFile(
                maximumSizeInBytes = posterImageProperties.maxSizeInBytes,
                actualSizeInBytes = multipartFile.size
            ).toFailure()
        }

        if (multipartFile.contentType?.startsWith("image/") != true) {
            return ErrorUploadingPoster.InvalidContentType(multipartFile.contentType).toFailure()
        }

        val bufferedImage = ImageIO.read(multipartFile.bytes.inputStream())
            ?: return ErrorUploadingPoster.NotAnImage.toFailure()

        val rgbImage = BufferedImage(bufferedImage.width, bufferedImage.height, BufferedImage.TYPE_INT_RGB).apply {
            graphics.drawImage(bufferedImage, 0, 0, Color.WHITE, null)
        }

        val resized = Scalr.resize(rgbImage, Scalr.Mode.FIT_TO_WIDTH, posterImageProperties.widthInPx)

        val newFileName = "${UUID.randomUUID()}.jpg"
        val outputFile = File("${getCanonicalFilePathBase()}/$newFileName")


        ImageIO.write(resized, "jpg", outputFile)

        return Success(newFileName)
    }

    fun getImageAsByteArrayOrNull(fileName: String): ByteArray? =
        ClassPathResource("/mock/posters/${fileName}").takeIf { it.exists() }?.inputStream?.readAllBytes()

    fun delete(fileName: String) {
        File("${getCanonicalFilePathBase()}/${fileName}").delete()
    }
}