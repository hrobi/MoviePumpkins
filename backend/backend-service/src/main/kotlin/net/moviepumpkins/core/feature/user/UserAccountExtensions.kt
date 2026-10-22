package net.moviepumpkins.core.feature.user

fun UserAccount.mapToUserAccountEntity() = UserAccountEntity(
    username = username,
    firstName = firstName,
    lastName = lastName,
    email = email,
    displayName = displayName,
    role = role,
    enabled = enabled,
)