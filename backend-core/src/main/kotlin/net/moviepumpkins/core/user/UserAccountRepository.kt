package net.moviepumpkins.core.user

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserAccountRepository : JpaRepository<UserAccountEntity, Long> {
    fun existsByEmail(email: String): Boolean

    fun findFirstByUsername(username: String): SimpleUserView?
}