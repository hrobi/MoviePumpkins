package net.moviepumpkins.core.application.fakedata.disposableusers

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface DisposableUserAccountRepository : JpaRepository<DisposableUserAccount, String> {

    @Query("select dua.username from DisposableUserAccount dua")
    fun findAllUsernames(): List<String>

}