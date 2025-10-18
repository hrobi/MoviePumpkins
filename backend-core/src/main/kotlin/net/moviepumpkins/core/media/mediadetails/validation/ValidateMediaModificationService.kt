package net.moviepumpkins.core.media.mediadetails.validation

import io.konform.validation.Validation
import io.konform.validation.ValidationResult
import io.konform.validation.constraints.maxLength
import io.konform.validation.constraints.maximum
import io.konform.validation.constraints.minLength
import io.konform.validation.constraints.minimum
import io.konform.validation.constraints.uniqueItems
import io.konform.validation.onEach
import net.moviepumpkins.core.media.mediadetails.model.MediaModification
import net.moviepumpkins.core.media.mediadetails.model.MovieModification
import net.moviepumpkins.core.media.mediadetails.model.SeriesModification
import net.moviepumpkins.core.util.result.Failure
import net.moviepumpkins.core.util.result.Result
import net.moviepumpkins.core.util.result.Success
import org.springframework.stereotype.Component
import java.time.LocalDate

@Component
class ValidateMediaModificationService {

    private val possibleYearValidation = Validation<Int> {
        minimum(1900)
        maximum(LocalDate.now().year + 100)
    }

    private val mediaModificationValidation = Validation {

        val titleValidation = Validation<String> {
            minLength(3) hint "should be at least 3 characters"
            maxLength(200) hint "should be at most 200 characters"
        }

        MediaModification::title ifPresent {
            run(titleValidation)
        }
        MediaModification::description ifPresent {
            minLength(20) hint "should be at least 20 characters"
            maxLength(1000) hint "should be at most 1000 characters"
        }

        val stringListValidation = Validation<List<String>> {
            uniqueItems()
            onEach {
                minLength(3) hint "should be at least 3 characters"
                maxLength(100) hint "should be at most 100 characters"
            }
        }

        MediaModification::writers ifPresent {
            run(stringListValidation)
        }
        MediaModification::actors ifPresent {
            run(stringListValidation)
        }
        MediaModification::directors ifPresent {
            run(stringListValidation)
        }

        MediaModification::originalTitle ifPresent {
            dynamic {
                if (it.isNotEmpty()) {
                    run(titleValidation)
                }
            }
        }

        MediaModification::countries ifPresent {
            run(stringListValidation)
        }

        MediaModification::awards ifPresent {
            onEach {
                constrain("should be number of won awards, then the name of the award") {
                    val award = it.split(" ", limit = 2)
                    award.size == 2 && award[0].toIntOrNull() != null && award[1].length in (3..50)
                }
            }
        }

        val awardLikeValidation = Validation<Int> {
            dynamic {
                if (it != -1) {
                    minimum(0)
                    maximum(500)
                }
            }
        }

        MediaModification::totalWins ifPresent {
            run(awardLikeValidation)
        }

        MediaModification::totalNominations ifPresent {
            run(awardLikeValidation)
        }

    }

    private fun validateMovie(movieModification: MovieModification) = Validation {
        run(mediaModificationValidation)

        MovieModification::lengthInMinutes ifPresent {
            dynamic {
                if (it != -1) {
                    minimum(0)
                    maximum(60 * 10)
                }
            }
        }

        MovieModification::releaseYear ifPresent {
            run(possibleYearValidation)
        }
    }.validate(movieModification)

    private fun validateSeries(seriesModification: SeriesModification) = Validation {
        run(mediaModificationValidation)

        SeriesModification::seasons ifPresent {
            dynamic {
                if (it != -1) {
                    minimum(1)
                    maximum(1000)
                }
            }
        }

        SeriesModification::startedInYear ifPresent {
            run(possibleYearValidation)
        }

        SeriesModification::endedInYear ifPresent {
            dynamic {
                if (it != -1) {
                    run(possibleYearValidation)
                }
            }
        }
    }.validate(seriesModification)

    fun tryValidate(mediaModification: MediaModification): Result<Unit, ValidationResult<MediaModification>> {
        val validation = when (mediaModification) {
            is MovieModification -> validateMovie(mediaModification)
            is SeriesModification -> validateSeries(mediaModification)
        }
        if (!validation.isValid) {
            return Failure(validation)
        }
        return Success(Unit)
    }
}