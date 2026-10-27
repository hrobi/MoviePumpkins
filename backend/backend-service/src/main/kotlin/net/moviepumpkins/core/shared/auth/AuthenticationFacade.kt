package net.moviepumpkins.core.shared.auth

import net.moviepumpkins.core.feature.user.UserAccount
import net.moviepumpkins.core.feature.user.UserAccountRepository
import net.moviepumpkins.core.shared.Loader
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class AuthenticationFacade(
    private val userAccountRepository: UserAccountRepository,
) {

    @Transactional
    fun extractUserAccount(): UserAccount {
        val authentication = SecurityContextHolder.getContext().authentication ?: throw EmptySecurityContextException()
        val persistenceLoader = Loader { userAccountRepository.findById(authentication.name).orElse(null) }
        return UserAccount.compose(
            persistenceLoader,
            authentication,
        )
    }

}