package net.moviepumpkins.core.userreport.model

import net.moviepumpkins.core.app.model.UserAccount

data class AggregatedUserReports(
    val reported: UserAccount,
    val reportCount: Int,
)