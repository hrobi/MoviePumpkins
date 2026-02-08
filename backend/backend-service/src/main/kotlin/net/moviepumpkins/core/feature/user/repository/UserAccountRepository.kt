package net.moviepumpkins.core.feature.user.repository

import net.moviepumpkins.core.feature.user.entity.UserAccountEntity
import org.springframework.data.jpa.repository.JpaRepository

interface UserAccountRepository : JpaRepository<UserAccountEntity, String>