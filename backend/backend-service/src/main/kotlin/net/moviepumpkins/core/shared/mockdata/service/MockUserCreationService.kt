package net.moviepumpkins.core.shared.mockdata.service

import jakarta.transaction.Transactional
import net.moviepumpkins.core.feature.user.model.UserAccount
import net.moviepumpkins.core.feature.user.service.SimpleUserPersistenceService
import net.moviepumpkins.core.integration.keycloak.service.KeycloakService
import net.moviepumpkins.core.shared.fileconfig.facade.FileAsObjectReaderFacade
import net.moviepumpkins.core.shared.logging.util.logger
import net.moviepumpkins.core.shared.mockdata.dto.MockUserListDto
import org.springframework.boot.CommandLineRunner
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Component

@Component
class MockUserCreationService(
    private val simpleUserPersistenceService: SimpleUserPersistenceService,
    private val keycloakService: KeycloakService,
    private val fileAsObjectReaderFacade: FileAsObjectReaderFacade
) : CommandLineRunner {

    companion object {
        const val MOCK_USERS_PATH = "mockdata/users/users.json"
    }

    private fun createUser(account: UserAccount, password: String) {
        keycloakService.createEmailVerifiedAndEnabledUser(account, password)
        simpleUserPersistenceService.createUser(account)
    }

    @Transactional
    override fun run(vararg args: String?) {
        logger().info("Starting saving mock data!")

        val mockUserListDto = fileAsObjectReaderFacade
            .runCatching { readUTF8JsonFile<MockUserListDto>(ClassPathResource(MOCK_USERS_PATH).file) }
            .getOrElse {
                logger().error("Couldn't parse file $MOCK_USERS_PATH into ${MockUserListDto::class.java}!", it)
                return
            }

        for (mockUser in mockUserListDto.users) {
            val userAccountEntity = simpleUserPersistenceService.getUserAccountEntity(username = mockUser.username)
            if (userAccountEntity != null) {
                logger().info("Mock user {} already exists, skipping...", userAccountEntity.username)
                continue
            }
            try {
                logger().info("Trying to create {}", mockUser.username)

                val userAccount = with(mockUser) {
                    UserAccount(
                        username = username,
                        displayName = displayName,
                        firstName = firstName,
                        lastName = lastName,
                        email = email,
                        role = role,
                        enabled = true
                    )
                }

                createUser(userAccount, mockUserListDto.password)
                logger().info("User {} created successfully!", mockUser.username)
            } catch (e: Exception) {
                logger().error("Exception occurred while creating mock user!", e)
            }
        }
    }

}