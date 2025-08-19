package net.moviepumpkins.core.user

import net.moviepumpkins.core.integration.controllers.UsersProfileController
import net.moviepumpkins.core.integration.controllers.UsersProtectedController
import net.moviepumpkins.core.integration.models.GetProtectedUserResponse
import net.moviepumpkins.core.integration.models.GetUserProfileResponse
import net.moviepumpkins.core.integration.models.UpdateUserProfileRequest
import net.moviepumpkins.core.user.exception.UserNotFoundByUsernameException
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class UserController(
    private val userService: UserService
) : UsersProtectedController, UsersProfileController {

    override fun getUserProtected(username: String): ResponseEntity<GetProtectedUserResponse> {
        return userService.getUserProfileByUsername(username)
            ?.toGetProtectedUserResponse()
            ?.let { ResponseEntity.ok(it) }
            ?: throw UserNotFoundByUsernameException(username)
    }

    override fun updateUserProfile(
        updateUserProfileRequest: UpdateUserProfileRequest,
        username: String
    ): ResponseEntity<Unit> {
        val profileUpdate = updateUserProfileRequest.toUpdateUserProfileData()
        userService.validateUpdateUserProfile(profileUpdate)
        userService.updateUserProfile(username, profileUpdate)
        return ResponseEntity.noContent().build()
    }

    override fun getUserProfile(username: String): ResponseEntity<GetUserProfileResponse> {
        return userService.getUserProfileByUsername(username)
            ?.toGetUserProfileResponse()
            ?.let { ResponseEntity.ok(it) }
            ?: throw UserNotFoundByUsernameException(username)
    }

}