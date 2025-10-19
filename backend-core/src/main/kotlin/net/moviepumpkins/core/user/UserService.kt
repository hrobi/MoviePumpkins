package net.moviepumpkins.core.user

import jakarta.transaction.Transactional
import net.moviepumpkins.core.app.model.UserAccount
import net.moviepumpkins.core.app.model.UserRole
import net.moviepumpkins.core.app.model.UserView
import net.moviepumpkins.core.app.validation.validateReasoningOrThrow
import net.moviepumpkins.core.oauth.AuthorizationService
import net.moviepumpkins.core.oauth.keycloak.UpdateUserRepresentationData
import net.moviepumpkins.core.user.entity.DisabledUserEntity
import net.moviepumpkins.core.user.entity.DisabledUserRepository
import net.moviepumpkins.core.user.entity.UserAccountEntity
import net.moviepumpkins.core.user.entity.UserReinstatementEntity
import net.moviepumpkins.core.user.entity.UserReinstatementRepository
import net.moviepumpkins.core.user.exception.UserStateError
import net.moviepumpkins.core.user.exception.UserStateException
import net.moviepumpkins.core.user.mapping.UserReinstatementMapper
import net.moviepumpkins.core.user.model.UserProfileUpdate
import net.moviepumpkins.core.user.model.UserReinstatement
import net.moviepumpkins.core.user.repository.UserAccountRepository
import net.moviepumpkins.core.userreport.service.UserReportService
import net.moviepumpkins.core.util.getLogger
import net.moviepumpkins.core.util.jpa.getReferenceByIdOrThrow
import net.moviepumpkins.core.util.jpa.relevencyPageRequest
import org.springframework.stereotype.Service

private const val PAGE_SIZE = 5

@Service
class UserService(
    private val userAccountRepository: UserAccountRepository,
    private val disabledUserRepository: DisabledUserRepository,
    private val userReinstatementRepository: UserReinstatementRepository,
    private val authorizationService: AuthorizationService,
    private val userReportService: UserReportService,
) {
    val LOG = getLogger()

    private fun UserAccountRepository.tryGetReferenceById(id: String) =
        userAccountRepository.getReferenceByIdOrThrow(id) { UserStateException(UserStateError.UserDoesNotExist(id)) }


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

    @Transactional
    fun disableUser(disabledUsername: String, disablerUsername: String, reason: String) {
        val user = userAccountRepository.findById(disabledUsername).orElseThrow()
        user.enabled = false

        validateReasoningOrThrow(reason)

        if (disabledUsername == disablerUsername) {
            throw UserStateException(UserStateError.UserCannotDisableThemselves)
        }

        disabledUserRepository.save(
            DisabledUserEntity(
                username = disabledUsername,
                user = userAccountRepository.tryGetReferenceById(disabledUsername),
                disabler = userAccountRepository.tryGetReferenceById(disablerUsername),
                reason = reason
            )
        )
        userAccountRepository.flush()
    }

    @Transactional
    fun reenableUser(username: String) {
        val user = userAccountRepository.findById(username).orElseThrow()
        if (!user.enabled) {
            throw UserStateException(UserStateError.UserAlreadyEnabled)
        }
        user.enabled = true
        userAccountRepository.flush()
        userReportService.purgeReports(username)
        disabledUserRepository.deleteById(username)
    }

    @Transactional
    fun askForReinstatement(username: String, reason: String) {
        val disabledUser =
            disabledUserRepository.getReferenceByIdOrThrow(id = username) { throw UserStateException(UserStateError.UserAlreadyEnabled) }
        validateReasoningOrThrow(reason)
        userReinstatementRepository.save(
            UserReinstatementEntity(
                username = username,
                disabledUser = disabledUser,
                reason = reason
            )
        )
        userReinstatementRepository.flush()
    }

    @Transactional
    fun getReinstatementRequestsPaged(page: Int): List<UserReinstatement> {
        return userReinstatementRepository.findAllPaged(relevencyPageRequest(page))
            .map(UserReinstatementMapper::fromUserReinstatementEntity)
    }
}