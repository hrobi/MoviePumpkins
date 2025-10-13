package net.moviepumpkins.core.flavour.service

import net.moviepumpkins.core.flavour.model.MediaFlavour
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Component

@Component
class ReadFlavoursFromFileService {
    private val SEPARATOR = ";"

    fun readAllFlavours(): Set<MediaFlavour> {
        return ClassPathResource("/config/flavours.csv", javaClass)
            .file
            .useLines {
                it
                    .map { line ->
                        val (id, name, description) = line.split(SEPARATOR)
                        MediaFlavour(id, name, description)
                    }
                    .toSet()
            }
    }
}