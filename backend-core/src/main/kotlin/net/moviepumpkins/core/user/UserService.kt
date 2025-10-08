package net.moviepumpkins.core.user

import jakarta.transaction.Transactional
import net.moviepumpkins.core.app.model.UserAccount
import net.moviepumpkins.core.app.model.UserRole
import net.moviepumpkins.core.app.model.UserView
import net.moviepumpkins.core.oauth.AuthorizationService
import net.moviepumpkins.core.oauth.keycloak.UpdateUserRepresentationData
import net.moviepumpkins.core.user.entity.UserAccountEntity
import net.moviepumpkins.core.user.model.UserProfileUpdate
import net.moviepumpkins.core.user.repository.UserAccountRepository
import net.moviepumpkins.core.util.getLogger
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userAccountRepository: UserAccountRepository,
    private val authorizationService: AuthorizationService,
) {
    val LOG = getLogger()

    @Transactional
    fun createUser(userAccount: UserAccount) {
        userAccountRepository.save(
            UserAccountEntity(
                username = userAccount.username,
                fullName = userAccount.fullName,
                email = userAccount.email,
                displayName = userAccount.displayName,
                role = userAccount.role
            )
        )
        authorizationService.addAppUserRoleByUsername(userAccount.username)
    }

    @Transactional
    fun getUser(username: String): UserView? {
        LOG.info("START method getUser")
        val user = userAccountRepository.viewFirstByUsername(username)
        LOG.info("EXIT method getUser")
        return user
    }

    @Transactional
    fun getUserRoleByUsername(username: String): UserRole? {
        return userAccountRepository.getUserRoleByUsername(username)
    }

    @Transactional
    fun updateUserProfile(username: String, userProfileUpdate: UserProfileUpdate) {
        LOG.info("START method updateUserProfile")

        val user = userAccountRepository.findFirstByUsername(username)!!
        val (firstName, lastName) = userProfileUpdate.fullName.split(" ", limit = 2)
        authorizationService.updateUserByUsername(
            username,
            UpdateUserRepresentationData(
                firstName = firstName,
                lastName = lastName,
                email = userProfileUpdate.email,
                attributes = UpdateUserRepresentationData.UserAttributes(displayName = listOf(userProfileUpdate.displayName))
            )
        )

        user.email = userProfileUpdate.email
        user.fullName = userProfileUpdate.fullName
        user.displayName = userProfileUpdate.displayName
        LOG.info("EXIT method updateUserProfile")
    }
}