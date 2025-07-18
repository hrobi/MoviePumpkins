package net.moviepumpkins.core.general

import jakarta.persistence.MappedSuperclass
import jakarta.persistence.PrePersist
import jakarta.persistence.PreUpdate
import java.time.LocalDateTime

@MappedSuperclass
class BaseEntity {
    lateinit var createdAt: LocalDateTime
    lateinit var modifiedAt: LocalDateTime

    @PrePersist
    private fun setCreatedAtAndModifiedAtAsNow() {
        createdAt = LocalDateTime.now()
        modifiedAt = LocalDateTime.now()
    }

    @PreUpdate
    private fun setModifiedAtAsNow() {
        modifiedAt = LocalDateTime.now()
    }
}