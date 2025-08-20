package net.moviepumpkins.core.user

import jakarta.transaction.Transactional
import net.moviepumpkins.core.application.ErrorCode
import net.moviepumpkins.core.application.validations
import net.moviepumpkins.core.oauth.AuthorizationService
import net.moviepumpkins.core.oauth.keycloak.UpdateUserRepresentationData
import net.moviepumpkins.core.user.db.UserAccountRepository
import net.moviepumpkins.core.user.model.UpdateUserProfileData
import net.moviepumpkins.core.user.model.UserProfile
import net.moviepumpkins.core.utils.getAuthentication
import net.moviepumpkins.core.utils.throwUnauthorized
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
    fun updateUserProfile(username: String, data: UpdateUserProfileData) {
        if (getAuthentication().username != username) {
            throwUnauthorized("Only $username is able to modify their profile!")
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
    }

    fun validateUpdateUserProfile(profileUpdate: UpdateUserProfileData) {
        validations {
            validateSingleParameter(UpdateUserProfileData::email.name, ErrorCode.USER_PROPERTY_CONSTRAINT_01) {
                profileUpdate.email.matches(Regex("^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+\$"))
            }
            validateSingleParameter(
                UpdateUserProfileData::displayName.name,
                ErrorCode.USER_PROPERTY_CONSTRAINT_02
            ) {
                profileUpdate.displayName.trim() == profileUpdate.displayName
            }
            validateSingleParameter(
                UpdateUserProfileData::fullName.name,
                ErrorCode.USER_PROPERTY_CONSTRAINT_03
            ) {
                profileUpdate.fullName.trim() == profileUpdate.fullName
            }
            validateSingleParameter(
                UpdateUserProfileData::fullName.name,
                ErrorCode.USER_PROPERTY_CONSTRAINT_04
            ) {
                val nameParts = profileUpdate.fullName.split(" ")
                nameParts.size > 1 && nameParts.none { it.isEmpty() }
            }
        }
    }
}