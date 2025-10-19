package net.moviepumpkins.core.userreport.model

import net.moviepumpkins.core.app.model.UserAccount
import net.moviepumpkins.core.media.review.model.Review

data class UserReport(
    val reported: UserAccount,
    val reporter: UserAccount,
    val review: Review,
)
