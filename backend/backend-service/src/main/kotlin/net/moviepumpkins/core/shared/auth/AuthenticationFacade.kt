package net.moviepumpkins.core.shared.auth

import net.moviepumpkins.core.feature.user.UserAccount
import net.moviepumpkins.core.feature.user.application.SimpleUserPersistenceService
import net.moviepumpkins.core.shared.auth.EmptySecurityContextException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class AuthenticationFacade(
    private val simpleUserPersistenceService: SimpleUserPersistenceService,
) {

    @Transactional
    fun extractUserAccount(): UserAccount {
        val authentication = SecurityContextHolder.getContext().authentication ?: throw EmptySecurityContextException()
        return UserAccount.Companion.assemble(
            authentication = authentication,
            entity = lazy {
                simpleUserPersistenceService.getUserAccountEntity(authentication.name)
                    ?: throw IllegalStateException("User account ${authentication.name} not found despite user not being new!")
            },
        )
    }

}