package net.moviepumpkins.seeding

import jakarta.transaction.Transactional
import net.moviepumpkins.core.oauth.keycloak.NewUserRepresentationData
import net.moviepumpkins.core.oauth.keycloak.UseKeycloakAsAdminService
import net.moviepumpkins.core.oauth.keycloak.UserRepresentation
import net.moviepumpkins.core.user.db.UserAccountRepository
import net.moviepumpkins.core.user.model.UserProfile
import net.moviepumpkins.core.user.model.UserRole
import net.moviepumpkins.core.user.toUserAccountEntity
import net.moviepumpkins.core.utils.getLogger
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

@Component
@Profile("mockseed")
class SetVerifiedUsersMockDataTask(
    private val userAccountRepository: UserAccountRepository,
    private val useKeycloakAsAdmin: UseKeycloakAsAdminService,
) : CommandLineRunner {

    val LOG = getLogger()

    private val users = listOf(
        UserProfile(
            username = "jack-hoffman",
            fullName = "Jack Hoffman",
            email = "jack-hoffman@mailsfake.com",
            displayName = "Jack Hoffman",
            role = UserRole.REVIEWER
        ),
        UserProfile(
            username = "richard-rider",
            fullName = "Richard Rider",
            email = "richard-rider@mailsfake.com",
            displayName = "MR Advisor",
            role = UserRole.SUPERVISOR
        ),
        UserProfile(
            username = "emily-wokerson",
            fullName = "Emily Wokerson",
            email = "emilyw@mailsfake.com",
            displayName = "The Right One",
            role = UserRole.REVIEWER
        ),
        UserProfile(
            username = "chase-wood",
            fullName = "Chase Wood",
            email = "chase-wood@mailsfake.com",
            displayName = "Chase Wood",
            role = UserRole.REVIEWER
        ),
        UserProfile(
            username = "justin-case",
            fullName = "Justin Case",
            email = "justin-case@mailsfake.com",
            displayName = "Justin Case",
            role = UserRole.SUPERVISOR
        ),
        UserProfile(
            username = "cameron-geller",
            fullName = "Cameron Geller",
            email = "cameron-geller@mailsfake.com",
            displayName = "The Creator",
            role = UserRole.ADMIN
        ),
    )

    @Transactional
    override fun run(vararg args: String?) {

        if (args.contains("NoUsers")) {
            return
        }

        LOG.info("Seeding has started")

        userAccountRepository.saveAll(
            users.map { userProfile -> userProfile.toUserAccountEntity() }
        )

        LOG.info("Saved users in database")

        useKeycloakAsAdmin {
            for (user in users) {
                keycloakClient.addUser(
                    adminAccessToken, NewUserRepresentationData(
                        username = user.username,
                        email = user.email,
                        attributes = UserRepresentation.UserAttributes(displayName = listOf(user.displayName)),
                        firstName = user.fullName.split(" ")[0],
                        lastName = user.fullName.split(" ")[1],
                        realmRoles = listOf("app_user"),
                    )
                )
            }
        }

        LOG.info("Successfully saved users in authorization server")
    }

}