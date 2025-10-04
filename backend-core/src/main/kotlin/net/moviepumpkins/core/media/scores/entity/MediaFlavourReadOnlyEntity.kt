package net.moviepumpkins.core.media.scores.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import net.jcip.annotations.Immutable

@Entity
@Table(name = "media_flavour")
@Immutable
class MediaFlavourReadOnlyEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(updatable = false, insertable = false)
    var id: String?,

    val flavourName: String,

    val shortDescription: String
)