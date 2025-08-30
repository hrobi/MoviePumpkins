package net.moviepumpkins.core.user.repository

import net.moviepumpkins.core.user.entity.SimpleUserView
import net.moviepumpkins.core.user.entity.UserAccountEntity
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