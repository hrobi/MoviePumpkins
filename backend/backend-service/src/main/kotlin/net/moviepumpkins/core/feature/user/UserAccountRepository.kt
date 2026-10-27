package net.moviepumpkins.core.feature.user

import org.springframework.data.jpa.repository.JpaRepository

interface UserAccountRepository : JpaRepository<UserAccountEntity, String>