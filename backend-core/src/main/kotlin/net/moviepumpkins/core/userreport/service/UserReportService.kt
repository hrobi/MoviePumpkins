package net.moviepumpkins.core.userreport.service

import jakarta.transaction.Transactional
import net.moviepumpkins.core.media.review.entity.ReviewRepository
import net.moviepumpkins.core.user.repository.UserAccountRepository
import net.moviepumpkins.core.userreport.entity.UserReportAggregateRepository
import net.moviepumpkins.core.userreport.entity.UserReportAggregateView
import net.moviepumpkins.core.userreport.entity.UserReportEntity
import net.moviepumpkins.core.userreport.entity.UserReportRepository
import net.moviepumpkins.core.userreport.exception.UserReportStateError
import net.moviepumpkins.core.userreport.exception.UserReportStateException
import net.moviepumpkins.core.userreport.mapping.AggregatedUserReportsMapper
import net.moviepumpkins.core.userreport.mapping.UserReportMapper
import net.moviepumpkins.core.userreport.model.AggregatedUserReports
import net.moviepumpkins.core.userreport.model.UserReport
import net.moviepumpkins.core.userreport.projection.AggregatedReportsOnUserView
import net.moviepumpkins.core.util.jpa.getReferenceByIdOrThrow
import net.moviepumpkins.core.util.jpa.relevencyPageRequest
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Component

private const val PAGE_SIZE = 5

@Component
class UserReportService(
    private val userReportRepository: UserReportRepository,
    private val userReportAggregateRepository: UserReportAggregateRepository,
    private val userAccountRepository: UserAccountRepository,
    private val reviewRepository: ReviewRepository,
) {


    @Transactional
    fun reportUser(reporterUsername: String, reportedUsername: String, reviewId: Long) {
        userReportRepository.save(
            UserReportEntity(
                reporter = userAccountRepository.getReferenceById(reporterUsername),
                reported = userAccountRepository.getReferenceById(reportedUsername),
                review = reviewRepository.getReferenceById(reviewId)
            )
        )
    }

    @Transactional
    fun getAggregatedUserReportsPaged(page: Int): List<AggregatedUserReports> {
        return userReportAggregateRepository.findAllPaged(
            PageRequest.of(
                page,
                PAGE_SIZE,
                Sort.by(UserReportAggregateView::reportCount.name).descending()
            )
        ).map {
            AggregatedUserReportsMapper.fromUserReportAggregateView(it)
        }
    }

    @Transactional
    fun getReportedUserReportsPaged(reportedUsername: String, page: Int): List<UserReport> {
        return userReportRepository.findByReported(
            userAccountRepository.getReferenceById(reportedUsername),
            PageRequest.of(page, 5, Sort.by(UserReportEntity::createdAt.name).descending())
        ).map {
            UserReportMapper.fromUserReportEntity(it)
        }
    }

    @Transactional
    fun purgeReports(reportedUsername: String) {
        userReportRepository.deleteAllByReported(userAccountRepository.getReferenceById(reportedUsername))
    }

    @Transactional
    fun getReportedReviewsByCreatorPaged(creatorUsername: String, page: Int): List<AggregatedReportsOnUserView> {
        val creator = userAccountRepository.getReferenceByIdOrThrow(creatorUsername) {
            throw UserReportStateException(
                UserReportStateError.ReportedUserDoesNotExist
            )
        }
        return userReportRepository.findByReportedGroupByReview(creator, relevencyPageRequest(page))
    }
}