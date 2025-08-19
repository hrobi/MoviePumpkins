package net.moviepumpkins.core.user.db

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface UserAccountRepository : JpaRepository<UserAccountEntity, Long> {
    @Query("SELECT u FROM UserAccountEntity u WHERE u.username = :username")
    fun viewFirstByUsername(@Param("username") username: String): SimpleUserView?
    fun findFirstByUsername(username: String): UserAccountEntity?
}