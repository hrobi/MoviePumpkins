package net.moviepumpkins.core.flavour.entity

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "media_flavour")
class MediaFlavourEntity(
    @Id
    var id: String? = null,

    var flavourName: String,

    var shortDescription: String,
)