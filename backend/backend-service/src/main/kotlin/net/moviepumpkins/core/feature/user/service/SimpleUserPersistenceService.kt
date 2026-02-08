package net.moviepumpkins.core.feature.user.service

import jakarta.transaction.Transactional
import net.moviepumpkins.core.feature.user.entity.UserAccountEntity
import net.moviepumpkins.core.feature.user.extension.mapToUserAccountEntity
import net.moviepumpkins.core.feature.user.model.UserAccount
import net.moviepumpkins.core.feature.user.repository.UserAccountRepository
import net.moviepumpkins.core.shared.service.exception.ServiceError
import net.moviepumpkins.core.shared.service.exception.ServiceException
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
            throw ServiceException(
                ServiceError.ResourceNotFound("username", userAccount.username),
                code = "user.create.alreadyExists",
                message = "user already exists"
            )
        }
        return userAccountRepository.save(userAccount.mapToUserAccountEntity())
    }
}