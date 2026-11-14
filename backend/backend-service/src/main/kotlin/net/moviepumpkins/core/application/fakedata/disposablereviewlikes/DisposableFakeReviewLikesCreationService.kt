package net.moviepumpkins.core.application.fakedata.disposablereviewlikes

import jakarta.transaction.Transactional
import net.moviepumpkins.core.application.fakedata.disposablereviews.DisposableReviewRepository
import net.moviepumpkins.core.application.fakedata.disposableusers.DisposableUserAccountRepository
import net.moviepumpkins.core.feature.mediareview.likes.ReviewLikeId
import net.moviepumpkins.core.shared.logger
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Profile
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component

@Component
@Profile("mock")
@Order(4)
class DisposableFakeReviewLikesCreationService(
    private val disposableReviewLikeRepository: DisposableReviewLikeRepository,
    private val disposableUserAccountRepository: DisposableUserAccountRepository,
    private val disposableReviewRepository: DisposableReviewRepository,
) : CommandLineRunner {

    companion object {
        const val MAX_DISPOSABLE_LIKES_PER_REVIEW = 50
    }

    @Transactional
    override fun run(vararg args: String?) {
        val existingCount = disposableReviewLikeRepository.count()
        if (existingCount > 0) {
            logger().info("Found disposable review likes, count: {}", existingCount)
            return
        }

        val usernames = disposableUserAccountRepository.findAllUsernames()
        val reviewIds = disposableReviewRepository.findAllIds()

        val maxLikesPerReview = minOf(MAX_DISPOSABLE_LIKES_PER_REVIEW, usernames.size)
        val likes = reviewIds.flatMap { reviewId ->
            val likeCount = (0..maxLikesPerReview).random()
            usernames.take(likeCount).map { username ->
                DisposableReviewLike(ReviewLikeId(reviewId, username))
            }
        }

        disposableReviewLikeRepository.saveAll(likes)
        logger().info("Saved {} disposable review likes", likes.size)
    }
}
