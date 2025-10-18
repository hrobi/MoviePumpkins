package net.moviepumpkins.core.user.mapping

import net.moviepumpkins.core.app.model.UserAccount
import net.moviepumpkins.core.user.entity.UserAccountEntity

fun UserAccountEntity.toUserAccount() = UserAccount(
    username = username!!,
    role = role,
    email = email,
    displayName = displayName,
    fullName = fullName,
    isAppUser = true
)