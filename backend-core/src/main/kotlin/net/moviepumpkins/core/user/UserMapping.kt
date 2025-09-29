package net.moviepumpkins.core.user

import net.moviepumpkins.core.app.exception.checkRequest
import net.moviepumpkins.core.app.model.UserView
import net.moviepumpkins.core.integration.models.UpdateUserProfileRequest
import net.moviepumpkins.core.integration.models.UserProfile
import net.moviepumpkins.core.user.model.UserProfileUpdate
import net.moviepumpkins.core.util.checkEmail
import net.moviepumpkins.core.util.checkIsMultipleWords
import net.moviepumpkins.core.util.checkTrimmed

fun UserView.toUserProfile() = UserProfile(
    username = username,
    email = email,
    fullName = fullName,
    displayName = displayName,
    role = net.moviepumpkins.core.integration.models.UserRole.valueOf(role.name),
)

fun UpdateUserProfileRequest.toUpdateUserProfileDataOrThrow(): UserProfileUpdate {
    checkRequest {
        +checkEmail(::email)
        +checkTrimmed(::fullName)
        +checkIsMultipleWords(::fullName)
        +checkTrimmed(::displayName)
    }

    return UserProfileUpdate(
        email = email,
        displayName = displayName,
        fullName = fullName,
    )
}