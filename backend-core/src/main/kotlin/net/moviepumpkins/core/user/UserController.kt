package net.moviepumpkins.core.user

import net.moviepumpkins.core.app.config.AuthenticationFacade
import net.moviepumpkins.core.app.exception.ForbiddenException
import net.moviepumpkins.core.app.exception.NotFoundException
import net.moviepumpkins.core.integration.controllers.UsersController
import net.moviepumpkins.core.integration.controllers.UsersProfileController
import net.moviepumpkins.core.integration.models.UpdateUserProfileRequest
import net.moviepumpkins.core.integration.models.UserProfile
import net.moviepumpkins.core.user.validation.UserRequestsValidationService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import java.net.URI

@RestController
class UserController(
    private val userService: UserService,
    private val authenticationFacade: AuthenticationFacade,
    private val userRequestsValidationService: UserRequestsValidationService
) : UsersController, UsersProfileController {

    override fun updateUserProfile(
        updateUserProfileRequest: UpdateUserProfileRequest,
        username: String
    ): ResponseEntity<Unit> {
        userRequestsValidationService.validateUpdateUserProfileRequest(updateUserProfileRequest)
        val profileUpdate = updateUserProfileRequest.toUserProfileUpdate()
        if (username != authenticationFacade.authenticationName) {
            throw ForbiddenException()
        }
        userService.updateUserProfile(username, profileUpdate)
        return ResponseEntity.noContent().build()
    }

    override fun getUserProfile(username: String): ResponseEntity<UserProfile> {
        return userService.getUser(username)
            ?.toUserProfile()
            ?.let { ResponseEntity.ok(it) }
            ?: throw NotFoundException()
    }

    override fun createOwnUser(): ResponseEntity<UserProfile> {
        val username = authenticationFacade.authenticationName

        return if (authenticationFacade.isStoredUser) {
            ResponseEntity.ok(userService.getUser(username)!!.toUserProfile())
        } else {
            val userAccount = authenticationFacade.extractUserAccountFromJwt()
            userService.createUser(userAccount)
            ResponseEntity.created(URI.create("/users/${username}"))
                .body(userAccount.toUserProfile())
        }
    }

}