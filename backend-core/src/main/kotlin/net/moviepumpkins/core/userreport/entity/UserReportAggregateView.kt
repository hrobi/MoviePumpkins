package net.moviepumpkins.core.userreport.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import net.moviepumpkins.core.user.entity.UserAccountEntity
import org.hibernate.annotations.Immutable
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface UserReportAggregateRepository : JpaRepository<UserReportAggregateView, String> {
    @Query("SELECT urav FROM UserReportAggregateView urav")
    fun findAllPaged(pageable: Pageable): List<UserReportAggregateView>
}

@Entity
@Table(name = "user_report_aggregate_view")
@Immutable
class UserReportAggregateView(
    @Id
    @Column(name = "reported")
    val reportedUsername: String,

    @OneToOne
    @JoinColumn(name = "reported")
    val reported: UserAccountEntity,

    val reportCount: Int,
)