package net.moviepumpkins.core.media.mediadetails.entity

import com.vladmihalcea.hibernate.type.json.JsonBinaryType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import jakarta.persistence.Version
import net.moviepumpkins.core.app.entity.BaseEntity
import org.hibernate.annotations.JdbcTypeCode
import org.hibernate.annotations.Type
import org.hibernate.type.SqlTypes

@Entity
@Table(name = "media")
class MediaEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    var id: Long?,

    var title: String,

    var shortDescription: String,

    @JdbcTypeCode(SqlTypes.ARRAY)
    @Column(columnDefinition = "text[]")
    var directors: List<String>,

    @JdbcTypeCode(SqlTypes.ARRAY)
    @Column(columnDefinition = "text[]")
    var writers: List<String>,

    @JdbcTypeCode(SqlTypes.ARRAY)
    @Column(columnDefinition = "text[]")
    var actors: List<String>,

    var releaseYear: Int,

    var originalTitle: String?,

    @JdbcTypeCode(SqlTypes.ARRAY)
    @Column(name = "countries", columnDefinition = "text[]")
    var countries: List<String>?,

    @JdbcTypeCode(SqlTypes.ARRAY)
    @Column(name = "awards", columnDefinition = "text[]")
    var awards: List<String>?,

    var awardsWinCount: Int?,

    var nominationsCount: Int?,

    @Type(JsonBinaryType::class)
    @Column(columnDefinition = "jsonb")
    var otherDetails: MediaTypeSpecificDetails,

    @Enumerated(EnumType.STRING)
    var mediaType: MediaType,

    @Version
    var version: Int? = null,
) : BaseEntity()