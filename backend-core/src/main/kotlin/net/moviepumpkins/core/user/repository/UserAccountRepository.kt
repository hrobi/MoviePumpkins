package net.moviepumpkins.core.user.repository

import net.moviepumpkins.core.app.model.UserRole
import net.moviepumpkins.core.app.model.UserView
import net.moviepumpkins.core.user.entity.UserAccountEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface UserAccountRepository : JpaRepository<UserAccountEntity, Long> {
    @Query("SELECT u FROM UserAccountEntity u WHERE u.username = :username")
    fun viewFirstByUsername(@Param("username") username: String): UserView?

    @Query("SELECT u.role FROM UserAccountEntity u WHERE u.username = :username")
    fun getUserRoleByUsername(@Param("username") username: String): UserRole?
    fun findFirstByUsername(username: String): UserAccountEntity?
}