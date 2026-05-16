package net.moviepumpkins.core.feature.media.domain.model.entity

import jakarta.persistence.Column
import jakarta.persistence.Convert
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import net.moviepumpkins.core.feature.media.application.converter.SemicolonSeparatedStringListConverter
import net.moviepumpkins.core.feature.media.domain.model.value.MPARating
import net.moviepumpkins.core.feature.media.domain.model.value.MediaFormatData
import net.moviepumpkins.core.shared.className
import net.moviepumpkins.core.shared.exception.UnknownDiscriminatorException
import org.hibernate.annotations.UuidGenerator
import org.springframework.data.annotation.Version
import java.util.*

@Entity
@Table(name = "media")
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

    format: MediaFormatData,

    ) {

    @Column(name = "format")
    private lateinit var formatDiscriminator: String
    private var runtime: Int? = null
    private var numberOfSeasons: Int? = null
    private var lastYear: Int? = null

    @Version
    private var version: Long = 1

    init {
        setFormatLogic(format)
    }

    var format: MediaFormatData
        get() {
            return when (formatDiscriminator) {
                className<MediaFormatData.FeatureFilm>() -> MediaFormatData.FeatureFilm(
                    runtimeInMinutes = runtime,
                )

                className<MediaFormatData.Series>() -> MediaFormatData.Series(
                    numberOfSeasons = numberOfSeasons,
                    lastYear = lastYear,
                )

                else -> throw UnknownDiscriminatorException(
                    formatDiscriminator,
                    MediaFormatData::class.java
                )
            }
        }
        set(format) {
            setFormatLogic(format)
        }

    private fun setFormatLogic(format: MediaFormatData) {
        when (format) {
            is MediaFormatData.FeatureFilm -> {
                this.formatDiscriminator = className<MediaFormatData.FeatureFilm>()
                this.runtime = format.runtimeInMinutes
                this.lastYear = null
                this.numberOfSeasons = null
            }

            is MediaFormatData.Series -> {
                this.formatDiscriminator = className<MediaFormatData.Series>()
                this.numberOfSeasons = format.numberOfSeasons
                this.lastYear = format.lastYear
                this.runtime = null
            }
        }
    }

}