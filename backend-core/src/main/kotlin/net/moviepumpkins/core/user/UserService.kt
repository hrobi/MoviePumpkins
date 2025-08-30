package net.moviepumpkins.core.user

import jakarta.transaction.Transactional
import net.moviepumpkins.core.oauth.AuthorizationService
import net.moviepumpkins.core.oauth.keycloak.UpdateUserRepresentationData
import net.moviepumpkins.core.user.model.UserProfile
import net.moviepumpkins.core.user.model.UserProfilePropertyConstraintViolation
import net.moviepumpkins.core.user.model.UserProfileUpdate
import net.moviepumpkins.core.user.model.UserProfileUpdateForbiddenError
import net.moviepumpkins.core.user.repository.UserAccountRepository
import net.moviepumpkins.core.util.Failure
import net.moviepumpkins.core.util.Fallible
import net.moviepumpkins.core.util.Success
import net.moviepumpkins.core.util.getAuthentication
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userAccountRepository: UserAccountRepository,
    private val authorizationService: AuthorizationService,
) {

    @Transactional
    fun saveUser(userProfile: UserProfile) {
        userAccountRepository.save(userProfile.toUserAccountEntity())
    }

    @Transactional
    fun getUserProfileByUsername(username: String): UserProfile? {
        return userAccountRepository.viewFirstByUsername(username)?.toUserProfile()
    }

    @Transactional
    fun syncDatabaseUserWithAuthorizationServer(userProfile: UserProfile, sid: String) {
        saveUser(userProfile)
        authorizationService.addAppUserRoleBySid(sid)
    }

    @Transactional
    fun updateUserProfile(username: String, data: UserProfileUpdate): Fallible<UserProfileUpdateForbiddenError> {
        if (getAuthentication().username != username) {
            return Failure(UserProfileUpdateForbiddenError.CANNOT_MODIFY_OTHERS_PROFILE)
        }

        val user = userAccountRepository.findFirstByUsername(username)!!
        if (user.email != data.email) {
            user.email = data.email
        }

        if (user.fullName != data.fullName) {
            user.fullName = data.fullName
        }

        if (user.displayName != data.displayName) {
            user.displayName = data.displayName
        }

        val (firstName, lastName) = user.fullName.split(" ", limit = 2)
        authorizationService.updateUserByUsername(
            username,
            UpdateUserRepresentationData(
                firstName = firstName,
                lastName = lastName,
                email = data.email,
                attributes = UpdateUserRepresentationData.UserAttributes(displayName = listOf(data.displayName))
            )
        )
        return Success(Unit)
    }

    fun validateUpdateUserProfile(profileUpdate: UserProfileUpdate): Fallible<List<UserProfilePropertyConstraintViolation>> {

        val constraintViolationList = buildList<UserProfilePropertyConstraintViolation> {
            if (!profileUpdate.email.matches(Regex("^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+\$"))) {
                add(UserProfilePropertyConstraintViolation.EMAIL_PATTERN)
            }

            if (!profileUpdate.email.matches(Regex("^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+\$"))) {
                add(UserProfilePropertyConstraintViolation.EMAIL_PATTERN)
            }

            if (profileUpdate.displayName.trim() != profileUpdate.displayName) {
                add(UserProfilePropertyConstraintViolation.TRIMMED_DISPLAY_NAME)
            }

            if (profileUpdate.fullName.trim() != profileUpdate.fullName) {
                add(UserProfilePropertyConstraintViolation.TRIMMED_FULL_NAME)
            }

            val nameParts = profileUpdate.fullName.split(" ")
            if (nameParts.size <= 1 || nameParts.any { it.isEmpty() }) {
                add(UserProfilePropertyConstraintViolation.FULLNAME_SHOULD_CONTAIN_AT_LEAST_TWO_WORDS)
            }
        }

        return if (constraintViolationList.isNotEmpty()) {
            Failure(constraintViolationList)
        } else {
            Success(Unit)
        }
    }
}