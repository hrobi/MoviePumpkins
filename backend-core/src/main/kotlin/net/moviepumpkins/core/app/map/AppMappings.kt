package net.moviepumpkins.core.app.map

import net.moviepumpkins.core.app.model.UserAccount
import net.moviepumpkins.core.app.model.UserRole
import net.moviepumpkins.core.user.model.UserProfile

fun UserProfile.toUserAccount() = UserAccount(
    username = username,
    role = UserRole.valueOf(role.name)
)