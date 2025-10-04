package net.moviepumpkins.core.media.scores.db

import jakarta.persistence.*
import net.jcip.annotations.Immutable

@Entity
@Table(name = "media_flavour")
@Immutable
class MediaFlavourReadOnlyEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(updatable = false, insertable = false)
    var id: String?,

    var flavourName: String,

    var shortDescription: String
)