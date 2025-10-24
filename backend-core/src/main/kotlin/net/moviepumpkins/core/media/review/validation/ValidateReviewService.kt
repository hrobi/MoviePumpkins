package net.moviepumpkins.core.media.review.validation

import io.konform.validation.Validation
import io.konform.validation.ValidationResult
import io.konform.validation.constraints.maxLength
import io.konform.validation.constraints.minLength
import net.moviepumpkins.core.media.review.model.ReviewContent
import net.moviepumpkins.core.util.validations.trimmed
import org.springframework.stereotype.Component

@Component
class ValidateReviewService {
    fun validateReviewContent(reviewContent: ReviewContent): ValidationResult<ReviewContent> {
        return Validation {
            ReviewContent::title {
                trimmed()
                minLength(5)
                maxLength(100)
            }

            ReviewContent::content {
                trimmed()
                minLength(50)
                maxLength(2000)
            }
        }.validate(reviewContent)
    }
}