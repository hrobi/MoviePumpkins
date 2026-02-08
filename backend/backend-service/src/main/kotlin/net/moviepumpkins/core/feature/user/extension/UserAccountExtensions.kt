package net.moviepumpkins.core.feature.user.extension

import net.moviepumpkins.core.feature.user.entity.UserAccountEntity
import net.moviepumpkins.core.feature.user.model.UserAccount

fun UserAccount.mapToUserAccountEntity() = UserAccountEntity(
    username = username,
    firstName = firstName,
    lastName = lastName,
    email = email,
    displayName = displayName,
    role = role,
    enabled = enabled,
)