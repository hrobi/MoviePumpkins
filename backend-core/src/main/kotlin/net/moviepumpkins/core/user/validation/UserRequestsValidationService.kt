package net.moviepumpkins.core.user.validation

import io.konform.validation.Validation
import net.moviepumpkins.core.integration.models.UpdateUserProfileRequest
import net.moviepumpkins.core.util.validations.email
import net.moviepumpkins.core.util.validations.multipleWords
import net.moviepumpkins.core.util.validations.trimmed
import org.springframework.stereotype.Component

@Component
class UserRequestsValidationService {
    fun validateUpdateUserProfileRequest(updateUserProfileRequest: UpdateUserProfileRequest) =
        Validation<UpdateUserProfileRequest> {
            UpdateUserProfileRequest::email { email() }
            UpdateUserProfileRequest::fullName { trimmed() }
            UpdateUserProfileRequest::fullName { multipleWords() }
            UpdateUserProfileRequest::displayName { trimmed() }
        }.validate(updateUserProfileRequest).errors
}