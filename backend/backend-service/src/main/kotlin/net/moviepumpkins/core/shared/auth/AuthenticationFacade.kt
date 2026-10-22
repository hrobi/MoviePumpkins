package net.moviepumpkins.core.shared.auth

import net.moviepumpkins.core.feature.user.UserAccount
import net.moviepumpkins.core.feature.user.UserAccountRepository
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.util.function.Supplier

@Component
class AuthenticationFacade(
    private val userAccountRepository: UserAccountRepository,
) {

    @Transactional
    fun extractUserAccount(): UserAccount {
        val authentication = SecurityContextHolder.getContext().authentication ?: throw EmptySecurityContextException()
        val entitySupplier = Supplier { userAccountRepository.findById(authentication.name).orElse(null) }
        return UserAccount.compose(
            authentication,
            entitySupplier
        )
    }

}