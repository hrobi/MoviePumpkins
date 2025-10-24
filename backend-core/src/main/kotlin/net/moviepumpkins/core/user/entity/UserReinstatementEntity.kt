package net.moviepumpkins.core.user.entity

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface UserReinstatementRepository : JpaRepository<UserReinstatementEntity, String> {
    @Query("SELECT ure FROM UserReinstatementEntity ure")
    fun findAllPaged(pageable: Pageable): List<UserReinstatementEntity>
}

@Entity
@Table(name = "user_reinstatement")
class UserReinstatementEntity(
    @Id
    var username: String,

    @OneToOne
    @JoinColumn(name = "username")
    var disabledUser: DisabledUserEntity,

    var reason: String,
)