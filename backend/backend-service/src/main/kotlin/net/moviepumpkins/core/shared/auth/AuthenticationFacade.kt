package net.moviepumpkins.core.shared.auth

import net.moviepumpkins.core.feature.user.UserAccountDetails
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
    fun extractUserAccount(): UserAccountDetails {
        val authentication = SecurityContextHolder.getContext().authentication ?: throw EmptySecurityContextException()
        val persistenceLoader = Loader { userAccountRepository.findById(authentication.name).orElse(null) }
        return UserAccountDetails.compose(
            persistenceLoader,
            authentication,
        )
    }

}