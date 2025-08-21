package net.moviepumpkins.core.media.mediadetails.db

import com.vladmihalcea.hibernate.type.array.StringArrayType
import com.vladmihalcea.hibernate.type.json.JsonBinaryType
import jakarta.persistence.*
import net.moviepumpkins.core.general.BaseEntity
import org.hibernate.annotations.Type

@Entity
@Table(name = "media_details")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "media_type", discriminatorType = DiscriminatorType.STRING)
class MediaDetailsEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    var id: Long?,

    var title: String,

    var shortDescription: String,

    @Type(StringArrayType::class)
    @Column(columnDefinition = "text[]")
    var directors: List<String>,

    @Type(StringArrayType::class)
    @Column(columnDefinition = "text[]")
    var writers: List<String>,

    @Type(StringArrayType::class)
    @Column(columnDefinition = "text[]")
    var actors: List<String>,

    var releaseYear: Int,

    var originalTitle: String?,

    var country: String?,

    @Type(StringArrayType::class)
    @Column(columnDefinition = "text[]")
    var awards: List<String>,

    var awardsWinCount: Int?,

    var nominationsCount: Int?,

    @Type(JsonBinaryType::class)
    @Column(columnDefinition = "jsonb")
    protected var otherDetails: MediaAdditionalDetails,

    @Enumerated(EnumType.STRING)
    var mediaType: MediaType,

    @Version
    var version: Int? = null,
) : BaseEntity()