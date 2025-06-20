package net.moviepumpkins.core.user.db

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserAccountRepository : JpaRepository<UserAccountEntity, Long> {
    fun findFirstByUsername(username: String): SimpleUserView?
}