package net.moviepumpkins.core.user

import jakarta.transaction.Transactional
import net.moviepumpkins.core.oauth.AuthorizationService
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userAccountRepository: UserAccountRepository,
    private val authorizationService: AuthorizationService,
) {

    @Transactional
    fun syncUser(userAccount: UserAccount, sid: String, existsInDatabase: Boolean) {
        if (existsInDatabase || userAccountRepository.existsByEmail(userAccount.email)) {
            return
        }
        userAccountRepository.save(userAccount.toUserAccountEntity())
        authorizationService.addAppUserRoleByUserId(sid)
    }

    @Transactional
    fun getUserAccountFromDatabaseByUsername(username: String): UserAccount {
        return userAccountRepository.findFirstByUsername(username)?.toUserAccount()
            ?: throw IllegalStateException("User by username does not exist")
    }
}