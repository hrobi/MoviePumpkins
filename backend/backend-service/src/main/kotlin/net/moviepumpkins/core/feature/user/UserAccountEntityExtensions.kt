package net.moviepumpkins.core.feature.user

fun UserAccountEntity.mapToUserAccount() = UserAccount(
    username = username!!,
    displayName = displayName,
    firstName = firstName,
    lastName = lastName,
    email = email,
    role = role,
    enabled = enabled
)