package net.moviepumpkins.core.feature.user.extension

import net.moviepumpkins.core.feature.user.entity.UserAccountEntity
import net.moviepumpkins.core.feature.user.model.UserAccount

fun UserAccountEntity.mapToUserAccount() = UserAccount(
    username = username!!,
    displayName = displayName,
    firstName = firstName,
    lastName = lastName,
    email = email,
    role = role,
    enabled = enabled
)