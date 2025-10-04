package net.moviepumpkins.core.user.validation

import net.moviepumpkins.core.app.exception.checkRequest
import net.moviepumpkins.core.integration.models.UpdateUserProfileRequest
import net.moviepumpkins.core.util.checkEmail
import net.moviepumpkins.core.util.checkIsMultipleWords
import net.moviepumpkins.core.util.checkTrimmed
import org.springframework.stereotype.Component

@Component
class UserRequestsValidationService {
    fun validateUpdateUserProfileRequest(updateUserProfileRequest: UpdateUserProfileRequest) {
        checkRequest {
            +checkEmail(updateUserProfileRequest::email)
            +checkTrimmed(updateUserProfileRequest::fullName)
            +checkIsMultipleWords(updateUserProfileRequest::fullName)
            +checkTrimmed(updateUserProfileRequest::displayName)
        }
    }
}