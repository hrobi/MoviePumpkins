package net.moviepumpkins.core.user.model

import net.moviepumpkins.core.app.model.UserAccount

data class UserReinstatement(
    val user: UserAccount,
    val disabler: UserAccount,
    val disablingReason: String,
    val requestReason: String,
)
