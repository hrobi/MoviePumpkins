package net.moviepumpkins.core.feature.media

import jakarta.persistence.Convert
import jakarta.persistence.DiscriminatorColumn
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Inheritance
import jakarta.persistence.InheritanceType
import jakarta.persistence.Table
import net.moviepumpkins.core.shared.entity.SemicolonSeparatedStringListConverter
import org.hibernate.annotations.UuidGenerator
import org.springframework.data.annotation.Version
import java.util.UUID

@Entity
@Table(name = "media")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "format")
class MediaEntity(
    @field:Id
    @field:GeneratedValue(strategy = GenerationType.UUID)
    @field:UuidGenerator(style = UuidGenerator.Style.TIME)
    var id: UUID? = null,

    var title: String,

    var plot: String?,

    var releaseYear: Int?,

    @field:Enumerated(EnumType.STRING)
    var mpaRating: MPARating?,

    var awardWinCount: Int?,

    var awardNominationCount: Int?,

    @field:Convert(converter = SemicolonSeparatedStringListConverter::class)
    var directors: List<String>?,

    @field:Convert(converter = SemicolonSeparatedStringListConverter::class)
    var writers: List<String>?,

    @field:Convert(converter = SemicolonSeparatedStringListConverter::class)
    var actors: List<String>?,

    @field:Convert(converter = SemicolonSeparatedStringListConverter::class)
    var genres: List<String>?,

    var posterUrl: String?,

    var imdbId: String,

    @Enumerated(EnumType.STRING)
    val format: MediaFormat,

    ) {

    @Version
    private var version: Long = 1

    data class CommonData(
        val title: String,
        val plot: String?,
        val releaseYear: Int?,
        val mpaRating: MPARating?,
        val awardWinCount: Int?,
        val awardNominationCount: Int?,
        val directors: List<String>?,
        val writers: List<String>?,
        val actors: List<String>?,
        val genres: List<String>?,
        val posterUrl: String?,
        val imdbId: String,
    )

}