package net.moviepumpkins.core.feature.user.application

import jakarta.transaction.Transactional
import net.moviepumpkins.core.feature.user.UserAccount
import net.moviepumpkins.core.feature.user.UserAccountEntity
import net.moviepumpkins.core.feature.user.UserAccountRepository
import net.moviepumpkins.core.feature.user.mapToUserAccountEntity
import net.moviepumpkins.core.shared.error.ServiceException
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
            throw ServiceException.Companion.idConflict(
                code = "user.create.alreadyExists",
                idName = "username",
                idValue = userAccount.username
            )
        }
        return userAccountRepository.save(userAccount.mapToUserAccountEntity())
    }
}