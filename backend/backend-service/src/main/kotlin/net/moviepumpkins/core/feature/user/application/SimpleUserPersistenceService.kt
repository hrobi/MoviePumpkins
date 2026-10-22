package net.moviepumpkins.core.feature.user.application

import jakarta.transaction.Transactional
import net.moviepumpkins.core.feature.user.UserAccount
import net.moviepumpkins.core.feature.user.UserAccountEntity
import net.moviepumpkins.core.feature.user.UserAccountRepository
import net.moviepumpkins.core.feature.user.error.UserAlreadyExistsException
import net.moviepumpkins.core.feature.user.mapToUserAccountEntity
import org.springframework.stereotype.Component

@Component
class SimpleUserPersistenceService(
    val userAccountRepository: UserAccountRepository,
) {

    @Transactional
    fun getUserAccountEntity(username: String): UserAccountEntity? {
        return userAccountRepository
            .findById(username)
            .orElse(null)
    }

    @Transactional
    fun createUser(userAccount: UserAccount): UserAccountEntity {
        if (userAccountRepository.existsById(userAccount.username)) {
            throw UserAlreadyExistsException(userAccount.username)
        }
        return userAccountRepository.save(userAccount.mapToUserAccountEntity())
    }
}