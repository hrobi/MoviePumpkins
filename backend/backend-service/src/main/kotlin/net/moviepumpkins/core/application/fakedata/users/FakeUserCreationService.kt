package net.moviepumpkins.core.application.fakedata.users

import jakarta.transaction.Transactional
import net.moviepumpkins.core.application.fakedata.FileAsObjectReaderFacade
import net.moviepumpkins.core.application.integration.keycloak.KeycloakService
import net.moviepumpkins.core.feature.user.UserAccount
import net.moviepumpkins.core.feature.user.UserAccountRepository
import net.moviepumpkins.core.shared.logger
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Profile
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Component

@Component
@Profile("mock")
class FakeUserCreationService(
    private val userAccountRepository: UserAccountRepository,
    private val keycloakService: KeycloakService,
    private val fileAsObjectReaderFacade: FileAsObjectReaderFacade
) : CommandLineRunner {

    companion object {
        const val MOCK_USERS_PATH = "mockdata/users/users.json"
    }

    private fun createUser(account: UserAccount, password: String) {
        keycloakService.createEmailVerifiedAndEnabledUser(account, password)
        userAccountRepository.save(account.toEntity())
    }

    @Transactional
    override fun run(vararg args: String?) {
        logger().info("Starting saving mock data!")

        val fakeUserListDto = fileAsObjectReaderFacade
            .runCatching { readUTF8JsonFile<FakeUserListDto>(ClassPathResource(MOCK_USERS_PATH).file) }
            .getOrElse {
                logger().error("Couldn't parse file $MOCK_USERS_PATH into ${FakeUserListDto::class.java}!", it)
                return
            }

        for (mockUser in fakeUserListDto.users) {
            val userAccountEntity = userAccountRepository.findById(mockUser.username).orElse(null)
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

                createUser(userAccount, fakeUserListDto.password)
                logger().info("User {} created successfully!", mockUser.username)
            } catch (e: Exception) {
                logger().error("Exception occurred while creating mock user!", e)
            }
        }
    }

}