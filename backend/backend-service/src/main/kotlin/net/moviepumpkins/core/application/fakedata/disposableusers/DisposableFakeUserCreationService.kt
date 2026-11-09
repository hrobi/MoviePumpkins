package net.moviepumpkins.core.application.fakedata.disposableusers

import jakarta.transaction.Transactional
import net.datafaker.Faker
import net.moviepumpkins.core.shared.logger
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Profile
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import java.util.UUID

@Component
@Profile("mock")
@Order(2)
class DisposableFakeUserCreationService(
    private val disposableUserAccountRepository: DisposableUserAccountRepository
) : CommandLineRunner {

    companion object {
        const val DISPOSABLE_FAKE_LIMIT = 500
    }


    private val faker = Faker()

    @Transactional
    override fun run(vararg args: String?) {
        val existingCount = disposableUserAccountRepository.count().toInt()
        if (existingCount >= DISPOSABLE_FAKE_LIMIT) {
            logger().info("Disposable fake accounts already created.")
            return
        }

        val missingCount = DISPOSABLE_FAKE_LIMIT - existingCount
        disposableUserAccountRepository.saveAll(create(missingCount))
        logger().info("Saved $missingCount disposable fake accounts")
    }

    fun create(count: Int): List<DisposableUserAccount> = List(count) {
        val firstName = faker.name().firstName()
        val lastName = faker.name().lastName()

        DisposableUserAccount(
            username = "${firstName.toUsernamePart()}-${lastName.toUsernamePart()}-${UUID.randomUUID()}",
            firstName = firstName,
            lastName = lastName,
            email = faker.internet().emailAddress(),
            displayName = "$firstName $lastName"
        )
    }

    private fun String.toUsernamePart(): String =
        lowercase().filter { it in 'a'..'z' || it in '0'..'9' }.take(20).ifEmpty { "user" }

}
