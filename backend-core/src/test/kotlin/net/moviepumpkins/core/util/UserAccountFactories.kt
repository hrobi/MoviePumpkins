package net.moviepumpkins.core.util

import net.moviepumpkins.core.app.model.UserAccount
import net.moviepumpkins.core.app.model.UserRole

fun userAccount(
    username: String,
    role: UserRole,
    email: String = "",
    displayName: String = "",
    fullName: String = "",
    isAppUser: Boolean = true,
): UserAccount {
    return UserAccount(
        username = username,
        role = role,
        email = email,
        displayName = displayName,
        fullName = fullName,
        isAppUser = isAppUser
    )
}