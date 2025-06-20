package net.moviepumpkins.core.application

import net.moviepumpkins.core.application.model.UserAccount
import net.moviepumpkins.core.application.model.UserRole
import net.moviepumpkins.core.user.model.UserProfile

fun UserProfile.toUserAccount() = UserAccount(
    username = username,
    role = UserRole.valueOf(role.name)
)