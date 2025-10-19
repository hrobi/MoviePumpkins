package net.moviepumpkins.core.media.mediadetails.entity

import com.vladmihalcea.hibernate.type.json.JsonBinaryType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import net.moviepumpkins.core.app.entity.BaseEntity
import net.moviepumpkins.core.user.entity.UserAccountEntity
import org.hibernate.annotations.JdbcTypeCode
import org.hibernate.annotations.Type
import org.hibernate.type.SqlTypes
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface MediaModificationRepository : JpaRepository<MediaModificationEntity, Long> {
    fun existsByMediaAndUser(media: MediaEntity, user: UserAccountEntity): Boolean

    @Query("SELECT mme FROM MediaModificationEntity mme JOIN FETCH mme.media WHERE mme.id = :id")
    fun findByIdAndFetchMedia(id: Long): MediaModificationEntity?
}

@Entity
@Table(name = "media_modification")
class MediaModificationEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    var id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "media_id")
    var media: MediaEntity,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "username")
    var user: UserAccountEntity,

    var title: String?,

    var shortDescription: String?,

    var releaseYear: Int?,

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
) : BaseEntity()