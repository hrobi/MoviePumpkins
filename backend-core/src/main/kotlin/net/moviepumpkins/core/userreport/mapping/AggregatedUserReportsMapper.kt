package net.moviepumpkins.core.userreport.mapping

import net.moviepumpkins.core.user.mapping.toUserAccount
import net.moviepumpkins.core.userreport.entity.UserReportAggregateView
import net.moviepumpkins.core.userreport.model.AggregatedUserReports

object AggregatedUserReportsMapper {
    fun fromUserReportAggregateView(userReportAggregateView: UserReportAggregateView) = AggregatedUserReports(
        reported = userReportAggregateView.reported.toUserAccount(),
        reportCount = userReportAggregateView.reportCount
    )
}