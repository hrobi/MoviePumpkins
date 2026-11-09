package net.moviepumpkins.core.application.fakedata.disposablereviews

import jakarta.transaction.Transactional
import net.datafaker.Faker
import net.moviepumpkins.core.application.fakedata.disposableusers.DisposableUserAccountRepository
import net.moviepumpkins.core.feature.media.MediaRepository
import net.moviepumpkins.core.shared.logger
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Profile
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component

@Component
@Profile("mock")
@Order(3)
class DisposableFakeReviewsCreationService(
    private val disposableReviewRepository: DisposableReviewRepository,
    private val disposableUserAccountRepository: DisposableUserAccountRepository,
    private val mediaRepository: MediaRepository,
) : CommandLineRunner {

    companion object {
        const val DISPOSABLE_FAKE_REVIEW_COUNT = 50
    }

    private val faker = Faker()

    @Transactional
    override fun run(vararg args: String?) {
        val mediaList = mediaRepository.findAllById(1L..5L)
        if (mediaList.isEmpty()) {
            logger().error("No media with IDs between 1 and 5 found; skipping disposable fake reviews.")
            return
        }

        val disposableReviewCount = disposableReviewRepository.count()
        if (disposableReviewCount > 0) {
            logger().info("Found disposable reviews, count: {}", disposableReviewCount)
            return
        }
        val disposableUsers = disposableUserAccountRepository.findAll()
        for (media in mediaList) {
            val disposableReviews = disposableUsers.take(50).map { user ->
                DisposableReview(
                    title = faker.lorem().sentence().take(50),
                    content = faker.bojackHorseman().tongueTwisters(),
                    reviewer = user,
                    media = media,
                )
            }
            disposableReviewRepository.saveAll(disposableReviews)
        }
    }

}