package net.moviepumpkins.core.feature.user.application

import jakarta.transaction.Transactional
import net.moviepumpkins.core.api.models.UserRoleEnum
import net.moviepumpkins.core.feature.user.UserAccountRepository
import net.moviepumpkins.core.feature.user.error.UserAlreadyExistsException
import net.moviepumpkins.core.feature.user.error.UserNotFoundException
import net.moviepumpkins.core.shared.auth.AuthenticationFacade
import org.springframework.stereotype.Component

@Component
class SimpleUserManagementService(
    private val userAccountRepository: UserAccountRepository,
    private val authenticationFacade: AuthenticationFacade,
) {

    @Transactional
    fun createUserAndGetUsername(): String {
        val userAccount = authenticationFacade.extractUserAccount()
        if (userAccountRepository.existsById(userAccount.username)) {
            throw UserAlreadyExistsException(userAccount.username)
        }
        userAccountRepository.save(userAccount.toEntity())
        return userAccount.username
    }

    @Transactional
    fun getUserRoleByUsername(username: String): UserRoleEnum {
        val user = userAccountRepository.findById(username).orElseThrow { UserNotFoundException(username) }
        return UserRoleEnum.valueOf(user.role.name)
    }

}