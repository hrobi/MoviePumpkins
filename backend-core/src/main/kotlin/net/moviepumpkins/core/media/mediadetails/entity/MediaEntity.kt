package net.moviepumpkins.core.media.mediadetails.entity

import com.vladmihalcea.hibernate.type.json.JsonBinaryType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Inheritance
import jakarta.persistence.InheritanceType
import jakarta.persistence.Table
import jakarta.persistence.Version
import net.moviepumpkins.core.app.entity.BaseEntity
import org.hibernate.annotations.JdbcTypeCode
import org.hibernate.annotations.Type
import org.hibernate.type.SqlTypes
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface MediaRepository : JpaRepository<MediaEntity, Long> {
    @Query("SELECT me.posterFile FROM MediaEntity me WHERE me.id = :id")
    fun findPosterFileById(@Param("id") id: Long): String?

    @Query("SELECT me.id FROM MediaEntity me WHERE me.title = :title")
    fun getIdByTitleOrNull(title: String): Long?
}

@Entity
@Table(name = "media")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
class MediaEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    var id: Long? = null,

    var title: String,

    var shortDescription: String,

    var releaseYear: Int,

    @JdbcTypeCode(SqlTypes.ARRAY)
    @Column(columnDefinition = "text[]")
    var directors: List<String>?,

    @JdbcTypeCode(SqlTypes.ARRAY)
    @Column(columnDefinition = "text[]")
    var writers: List<String>?,

    @JdbcTypeCode(SqlTypes.ARRAY)
    @Column(columnDefinition = "text[]")
    var actors: List<String>?,


    var originalTitle: String?,

    @JdbcTypeCode(SqlTypes.ARRAY)
    @Column(name = "countries", columnDefinition = "text[]")
    var countries: List<String>?,

    @JdbcTypeCode(SqlTypes.ARRAY)
    @Column(name = "awards", columnDefinition = "text[]")
    var awards: List<String>?,

    var awardsWinCount: Int?,

    var nominationsCount: Int?,

    var posterFile: String?,

    @Type(JsonBinaryType::class)
    @Column(columnDefinition = "jsonb")
    var otherDetails: MediaTypeSpecificDetails?,

    @Enumerated(EnumType.STRING)
    var mediaType: MediaType,


    @Version
    var version: Int = 0,
) : BaseEntity()