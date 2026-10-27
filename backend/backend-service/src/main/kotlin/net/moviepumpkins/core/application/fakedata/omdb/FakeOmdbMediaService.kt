package net.moviepumpkins.core.application.fakedata.omdb

import jakarta.transaction.Transactional
import net.moviepumpkins.core.application.fakedata.FileAsObjectReaderFacade
import net.moviepumpkins.core.application.integration.omdb.dto.OmdbMediaDto
import net.moviepumpkins.core.feature.media.MediaRepository
import net.moviepumpkins.core.feature.media.omdb.OmdbMediaDtoMapper
import net.moviepumpkins.core.shared.logger
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Profile
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Component

@Component
@Profile("mock")
class FakeOmdbMediaService(
    private val mediaRepository: MediaRepository,
    private val fileAsObjectReaderFacade: FileAsObjectReaderFacade,
) : CommandLineRunner {

    companion object {
        const val MOCK_MEDIA_PATH = "mockdata/media"
    }

    private fun loadAllNewMockedMediaIntoDB() {
        logger().info("Reading media mock data directory")
        val mediaFiles = ClassPathResource(MOCK_MEDIA_PATH).file
            .listFiles { file -> file.extension == "json" }
            ?: emptyArray()

        val dtos = mediaFiles.map { fileAsObjectReaderFacade.readUTF8JsonFile<OmdbMediaDto>(it) }

        val existingImdbIds = mediaRepository
            .findAllByImdbIdIn(dtos.map { it.imdbId })
            .map { it.imdbId }
            .toSet()

        val newEntities = dtos
            .filter { it.imdbId !in existingImdbIds }
            .map { OmdbMediaDtoMapper.mapToMediaEntity(it) }

        if (newEntities.isNotEmpty()) {
            logger().info("Saving new mock media entities ...")
        } else {
            logger().info("No new mock media found ...")
        }

        mediaRepository.saveAll(newEntities)

        logger().info("All mock data saved")
    }

    @Transactional
    override fun run(vararg args: String?) {
        loadAllNewMockedMediaIntoDB()
    }

}