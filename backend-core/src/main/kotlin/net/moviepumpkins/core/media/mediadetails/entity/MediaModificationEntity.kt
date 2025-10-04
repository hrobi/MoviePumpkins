package net.moviepumpkins.core.media.mediadetails.entity

import com.vladmihalcea.hibernate.type.json.JsonBinaryType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import net.moviepumpkins.core.app.entity.BaseEntity
import org.hibernate.annotations.JdbcTypeCode
import org.hibernate.annotations.Type
import org.hibernate.type.SqlTypes

@Entity
@Table(name = "media_modification")
class MediaModificationEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    var id: Long?,

    @ManyToOne
    var media: MediaEntity,

    var title: String?,

    var shortDescription: String?,

    @JdbcTypeCode(SqlTypes.ARRAY)
    @Column(columnDefinition = "text[]")
    var directors: List<String>?,

    @JdbcTypeCode(SqlTypes.ARRAY)
    @Column(columnDefinition = "text[]")
    var writers: List<String>?,

    @JdbcTypeCode(SqlTypes.ARRAY)
    @Column(columnDefinition = "text[]")
    var actors: List<String>?,

    var releaseYear: Int?,

    var originalTitle: String?,

    @JdbcTypeCode(SqlTypes.ARRAY)
    @Column(name = "countries", columnDefinition = "text[]")
    var countries: List<String>?,

    @JdbcTypeCode(SqlTypes.ARRAY)
    @Column(columnDefinition = "text[]")
    var awards: List<String>?,

    var awardsWinCount: Int?,

    var nominationsCount: Int?,

    @Type(JsonBinaryType::class)
    @Column(columnDefinition = "jsonb")
    protected var otherDetails: MediaTypeSpecificDetails,

    @Enumerated(EnumType.STRING)
    var mediaType: MediaType,
) : BaseEntity()