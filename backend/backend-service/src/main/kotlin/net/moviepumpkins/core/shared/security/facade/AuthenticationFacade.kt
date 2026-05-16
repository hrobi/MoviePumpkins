package net.moviepumpkins.core.shared.security.facade

import net.moviepumpkins.core.feature.user.model.UserAccount
import net.moviepumpkins.core.feature.user.service.SimpleUserPersistenceService
import net.moviepumpkins.core.shared.security.exception.EmptySecurityContextException
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
        return UserAccount.assemble(
            authentication = authentication,
            entity = lazy {
                simpleUserPersistenceService.getUserAccountEntity(authentication.name)
                    ?: throw IllegalStateException("User account ${authentication.name} not found despite user not being new!")
            },
        )
    }

}