package net.moviepumpkins.core.user

import net.moviepumpkins.core.app.exception.ClientErrorException
import net.moviepumpkins.core.app.map.toErrorCode
import net.moviepumpkins.core.integration.controllers.UsersProfileController
import net.moviepumpkins.core.integration.controllers.UsersProtectedController
import net.moviepumpkins.core.integration.models.*
import net.moviepumpkins.core.user.exception.UserNotFoundByUsernameExceptionBasic
import net.moviepumpkins.core.user.model.UserProfilePropertyConstraintViolation
import net.moviepumpkins.core.user.model.UserProfileUpdateForbiddenError
import net.moviepumpkins.core.util.Failure
import org.springframework.http.HttpStatus
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
            ?: throw UserNotFoundByUsernameExceptionBasic(username)
    }

    override fun updateUserProfile(
        updateUserProfileRequest: UpdateUserProfileRequest,
        username: String
    ): ResponseEntity<Unit> {
        val profileUpdate = updateUserProfileRequest.toUpdateUserProfileData()
        val validationResult = userService.validateUpdateUserProfile(profileUpdate)
        if (validationResult is Failure) {
            throw ClientErrorException(
                status = HttpStatus.BAD_REQUEST,
                body = UpdateUserProfileESC400R(
                    UpdateUserProfileESC400RStatus.BAD_REQUEST,
                    validationResult.error.map {
                        val fields = when (it) {
                            UserProfilePropertyConstraintViolation.EMAIL_PATTERN ->
                                listOf(UpdateUserProfileRequest::email.name)

                            UserProfilePropertyConstraintViolation.TRIMMED_DISPLAY_NAME ->
                                listOf(UpdateUserProfileRequest::displayName.name)

                            UserProfilePropertyConstraintViolation.TRIMMED_FULL_NAME ->
                                listOf(UpdateUserProfileRequest::fullName.name)

                            UserProfilePropertyConstraintViolation.FULLNAME_SHOULD_CONTAIN_AT_LEAST_TWO_WORDS ->
                                listOf(UpdateUserProfileRequest::fullName.name)
                        }
                        val errorCode = it.toErrorCode()
                        BadRequestBodyError(
                            fields = fields,
                            reason = errorCode.message,
                            errorCode = errorCode.code
                        )
                    }
                )
            )
        }
        val updateResult = userService.updateUserProfile(username, profileUpdate)
        if (updateResult is Failure) {
            when (updateResult.error) {
                UserProfileUpdateForbiddenError.CANNOT_MODIFY_OTHERS_PROFILE -> throw ClientErrorException(
                    status = HttpStatus.FORBIDDEN,
                    body = UpdateUserProfileESC403R(
                        status = UpdateUserProfileESC403RStatus.FORBIDDEN,
                        error = Status4xxResponse(reason = "You can only modify your own profile!")
                    )
                )
            }
        }
        return ResponseEntity.noContent().build()
    }

    override fun getUserProfile(username: String): ResponseEntity<GetUserProfileResponse> {
        return userService.getUserProfileByUsername(username)
            ?.toGetUserProfileResponse()
            ?.let { ResponseEntity.ok(it) }
            ?: throw UserNotFoundByUsernameExceptionBasic(username)
    }

}