package net.moviepumpkins.core.userreport.mapping

import net.moviepumpkins.core.media.review.mapping.toReview
import net.moviepumpkins.core.user.mapping.toUserAccount
import net.moviepumpkins.core.userreport.entity.UserReportEntity
import net.moviepumpkins.core.userreport.model.UserReport

object UserReportMapper {
    fun fromUserReportEntity(userReportEntity: UserReportEntity) = UserReport(
        reported = userReportEntity.reported.toUserAccount(),
        reporter = userReportEntity.reporter.toUserAccount(),
        review = userReportEntity.review.toReview(),
    )
}