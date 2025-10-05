package net.moviepumpkins.core.flavoursetup.service

import jakarta.persistence.EntityManager
import jakarta.transaction.Transactional
import net.moviepumpkins.core.flavoursetup.entity.MediaFlavourEntity
import net.moviepumpkins.core.flavoursetup.model.MediaFlavour
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Component

@Component
class FlavoursSetupService(
    private val entityManager: EntityManager
) : ApplicationRunner {

    private val SEPARATOR = ";"

    @Transactional
    override fun run(args: ApplicationArguments?) {

        val mediaFlavourEntitiesFromDb =
            entityManager.createQuery(
                "SELECT fe FROM MediaFlavourEntity fe ORDER BY fe.id",
                MediaFlavourEntity::class.java
            )
                .resultList
                .groupBy(MediaFlavourEntity::id)
                .mapValues { (_, value) -> value[0] }

        val mediaFlavoursFromFile = ClassPathResource("/config/flavours.csv", FlavoursSetupService::class.java)
            .file
            .useLines {
                it
                    .map { line ->
                        val (id, name, description) = line.split(SEPARATOR)
                        MediaFlavour(id, name, description)
                    }
                    .toList()
            }

        for ((id, name, description) in mediaFlavoursFromFile) {
            val entity = mediaFlavourEntitiesFromDb[id]
            if (entity != null) {
                entity.flavourName = name
                entity.shortDescription = description
            } else {
                entityManager.persist(MediaFlavourEntity(id, name, description))
            }
        }

    }

}