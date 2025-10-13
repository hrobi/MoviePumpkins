package net.moviepumpkins.core.flavour.service

import jakarta.persistence.EntityManager
import jakarta.transaction.Transactional
import net.moviepumpkins.core.flavour.entity.MediaFlavourEntity
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component

@Component
class FlavoursSetupService(
    private val readFlavoursFromFileService: ReadFlavoursFromFileService,
    private val entityManager: EntityManager,
) : ApplicationRunner {

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

        val mediaFlavoursFromFile = readFlavoursFromFileService.readAllFlavours()

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