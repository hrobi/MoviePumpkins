package net.moviepumpkins.core.shared.entity

import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.MappedSuperclass
import jakarta.persistence.PrePersist
import jakarta.persistence.PreUpdate
import java.time.OffsetDateTime

@MappedSuperclass
abstract class EntityBase {

    @field:Id
    @field:GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    var createdAt: OffsetDateTime = OffsetDateTime.now()
        protected set
    var updatedAt: OffsetDateTime = OffsetDateTime.now()
        protected set

    @PrePersist
    fun onPersist() {
        createdAt = OffsetDateTime.now()
        updatedAt = OffsetDateTime.now()
    }

    @PreUpdate
    fun onUpdate() {
        updatedAt = OffsetDateTime.now()
    }

}