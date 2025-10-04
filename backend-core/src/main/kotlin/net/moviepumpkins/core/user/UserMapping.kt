package net.moviepumpkins.core.user

import net.moviepumpkins.core.app.model.UserView
import net.moviepumpkins.core.integration.models.UpdateUserProfileRequest
import net.moviepumpkins.core.integration.models.UserProfile
import net.moviepumpkins.core.user.model.UserProfileUpdate

fun UserView.toUserProfile() = UserProfile(
    username = username,
    email = email,
    fullName = fullName,
    displayName = displayName,
    role = net.moviepumpkins.core.integration.models.UserRole.valueOf(role.name),
)

fun UpdateUserProfileRequest.toUserProfileUpdate(): UserProfileUpdate {
    return UserProfileUpdate(
        email = email,
        displayName = displayName,
        fullName = fullName,
    )
}