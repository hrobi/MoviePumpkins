package net.moviepumpkins.core.user

import net.moviepumpkins.core.integration.models.GetProtectedUserResponse
import net.moviepumpkins.core.integration.models.GetUserProfileResponse
import net.moviepumpkins.core.integration.models.UpdateUserProfileRequest
import net.moviepumpkins.core.integration.models.UserRole
import net.moviepumpkins.core.user.entity.SimpleUserView
import net.moviepumpkins.core.user.entity.UserAccountEntity
import net.moviepumpkins.core.user.model.UserProfile
import net.moviepumpkins.core.user.model.UserProfileUpdate

fun SimpleUserView.toUserProfile() = UserProfile(
    username = username,
    email = email,
    fullName = fullName,
    displayName = displayName,
    role = role,
)

fun UserProfile.toUserAccountEntity() = UserAccountEntity(
    username = username,
    email = email,
    fullName = fullName,
    displayName = displayName,
    role = role,
)

fun UserProfile.toGetProtectedUserResponse() = GetProtectedUserResponse(
    username = username,
    displayName = displayName,
    role = UserRole.valueOf(role.name),
)

fun UserProfile.toGetUserProfileResponse() = GetUserProfileResponse(
    username = username,
    fullName = fullName,
    email = email,
    displayName = displayName,
    role = UserRole.valueOf(role.name)
)

fun UpdateUserProfileRequest.toUpdateUserProfileData() = UserProfileUpdate(
    email = email,
    displayName = displayName,
    fullName = fullName,
)