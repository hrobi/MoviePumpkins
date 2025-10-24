package net.moviepumpkins.core.userreport.entity

import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import net.moviepumpkins.core.app.entity.BaseEntity
import net.moviepumpkins.core.media.review.entity.ReviewEntity
import net.moviepumpkins.core.user.entity.UserAccountEntity
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface UserReportRepository : JpaRepository<UserReportEntity, Long> {

    @Query(
        """
            SELECT ure 
            FROM UserReportEntity ure 
            JOIN FETCH ure.reporter 
            JOIN FETCH ure.reported
            JOIN FETCH ure.review 
            WHERE ure.reported = :reported"""
    )
    fun findByReported(reported: UserAccountEntity, pageable: Pageable): List<UserReportEntity>

    fun deleteAllByReported(reported: UserAccountEntity)

//    @Query(
//        """
//            SELECT
//                ure.review as review,
//                COUNT(ure.reporter) as reportCount
//            FROM UserReportEntity ure
//            JOIN FETCH ure.reported
//            JOIN FETCH ure.review
//            WHERE ure.reported = :reported
//            GROUP BY ure.review
//            ORDER BY reportCount DESC
//        """
//    )
//    fun findByReportedGroupByReview(reported: UserAccountEntity, pageable: Pageable): List<AggregatedReportsOnUserView>
}

@Entity
@Table(name = "user_report")
class UserReportEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    var id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reporter")
    var reporter: UserAccountEntity,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reported")
    var reported: UserAccountEntity,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id")
    var review: ReviewEntity,
) : BaseEntity()