package net.moviepumpkins.core.application.usecase

import net.moviepumpkins.core.oauth.AuthorizationService
import net.moviepumpkins.core.user.UserService
import net.moviepumpkins.core.user.model.UserProfile
import org.springframework.stereotype.Service


@Service
class SyncUserUseCase(
    private val userService: UserService,
    private val authorizationService: AuthorizationService,
) {
    operator fun invoke(userProfile: UserProfile, sid: String) {
        userService.saveUser(userProfile)
        authorizationService.addAppUserRoleBySid(sid)
    }
}