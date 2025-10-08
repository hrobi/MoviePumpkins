package net.moviepumpkins.core.media.review

import io.zonky.test.db.AutoConfigureEmbeddedDatabase
import net.moviepumpkins.core.media.mediadetails.entity.MediaRepository
import net.moviepumpkins.core.media.review.model.ErrorFindingPagedReviews
import net.moviepumpkins.core.media.review.model.Review
import net.moviepumpkins.core.media.review.model.ReviewContent
import net.moviepumpkins.core.util.result.Failure
import net.moviepumpkins.core.util.result.Success
import org.flywaydb.test.annotation.FlywayTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.junit.jupiter.api.assertInstanceOf
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.jdbc.Sql
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

@SpringBootTest
@FlywayTest
@AutoConfigureEmbeddedDatabase
class ReviewServiceTest {

    @Autowired
    private lateinit var reviewService: ReviewService

    @Autowired
    private lateinit var mediaRepository: MediaRepository

    data class TestComparableReviewView(
        val reviewerUsername: String,
        val title: String,
        val content: String,
        val likes: Int,
        val dislikes: Int,
    ) {
        companion object {
            fun fromReview(review: Review) = TestComparableReviewView(
                reviewerUsername = review.creator.username,
                title = review.title,
                content = review.content,
                likes = review.likes,
                dislikes = review.dislikes
            )
        }
    }

    companion object {

        val testComparableReviewView1 = TestComparableReviewView(
            reviewerUsername = "user7",
            title = "Great and well directed Adventure/Drama",
            content = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aliquam odio quam, fringilla eu consectetur eget, lacinia rutrum odio. Sed lobortis elementum congue. Cras at massa dolor. Morbi metus ex, euismod sit amet leo semper, elementum pharetra nisl. Curabitur pretium risus non nisi tristique, vitae finibus tellus pretium. Duis eu ultrices ante. Praesent massa ex, volutpat nec magna ac, vehicula sollicitudin quam. Integer vel est eget sapien finibus dignissim. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas.",
            likes = 6,
            dislikes = 2
        )
        val testComparableReviewView2 = TestComparableReviewView(
            reviewerUsername = "user8",
            title = "This is only the beginning!",
            content = "Cras lacinia ante at eros efficitur feugiat. In dapibus ut augue non laoreet. Etiam pharetra odio a magna placerat eleifend. Aenean et diam ac purus varius sodales ut vitae nulla. Cras consectetur pharetra tellus. Ut sodales elementum est et varius. In tincidunt lobortis urna eget feugiat. Phasellus sit amet mauris at metus cursus condimentum. Maecenas tristique odio nunc, vestibulum consequat ante auctor eu. Morbi id posuere orci. Quisque tempus lacinia nibh eget molestie. Pellentesque tristique bibendum consectetur. Pellentesque aliquet aliquet porttitor. Suspendisse interdum lacus sit amet velit egestas pellentesque.",
            likes = 5,
            dislikes = 3
        )
        val testComparableReviewView3 = TestComparableReviewView(
            reviewerUsername = "user9",
            title = "Visually stunning but mostly about setting the stage",
            content = "Donec eget justo molestie, suscipit dolor sed, varius ex. In suscipit accumsan nulla, vel interdum quam egestas eget. Sed dui quam, euismod sed nisl eu, luctus volutpat arcu. Sed vitae mi in dui tempor vestibulum laoreet luctus enim. Pellentesque accumsan, nisi sit amet aliquam fringilla, libero metus commodo sapien, ac ultricies metus ante pretium urna. Morbi nec mi a tortor sollicitudin gravida. Cras et purus vestibulum, pharetra arcu eget, rutrum tellus. Integer id mollis sapien. Nulla aliquet vitae ipsum sed consequat. Mauris risus neque, hendrerit ut mauris a, aliquet venenatis ante. Donec sit amet ante et urna accumsan eleifend sed a ante. Fusce quis sem ac nisi mollis sodales. Mauris augue mauris, mattis nec justo sit amet, tincidunt feugiat ante. Maecenas hendrerit tellus sed nulla iaculis suscipit. Fusce sollicitudin sodales fermentum.",
            likes = 4,
            dislikes = 4,
        )
        val testComparableReviewView4 = TestComparableReviewView(
            reviewerUsername = "user10",
            title = "A Masterpiece and a phenomenal adaptation.",
            content = "Aliquam sagittis mi eget metus tincidunt, id laoreet nunc accumsan. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia curae; Curabitur fringilla enim ut velit ultrices, eget ultrices turpis feugiat. Mauris in dictum nulla. Proin nisi elit, placerat id diam posuere, aliquam ornare risus. Sed eget augue eu erat tincidunt porttitor in aliquet odio. Proin malesuada eros vitae ex efficitur, ac tincidunt urna commodo. Integer ex sapien, scelerisque nec ornare at, maximus at massa. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos. Maecenas egestas risus id neque euismod sagittis.",
            likes = 0,
            dislikes = 0,
        )
        val testComparableReviewView5 = TestComparableReviewView(
            reviewerUsername = "richard-rider",
            title = "Great looking but without depth",
            content = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Aliquam odio quam, fringilla eu consectetur eget, lacinia rutrum odio. Sed lobortis elementum congue. Cras at massa dolor. Morbi metus ex, euismod sit amet leo semper, elementum pharetra nisl. Curabitur pretium risus non nisi tristique, vitae finibus tellus pretium. Duis eu ultrices ante. Praesent massa ex, volutpat nec magna ac, vehicula sollicitudin quam. Integer vel est eget sapien finibus dignissim. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas.",
            likes = 0,
            dislikes = 0,
        )

        val testComparableReviewView6 = TestComparableReviewView(
            reviewerUsername = "emily-wokerson",
            title = "Great and well directed Adventure/Drama",
            content = "Cras lacinia ante at eros efficitur feugiat. In dapibus ut augue non laoreet. Etiam pharetra odio a magna placerat eleifend. Aenean et diam ac purus varius sodales ut vitae nulla. Cras consectetur pharetra tellus. Ut sodales elementum est et varius. In tincidunt lobortis urna eget feugiat. Phasellus sit amet mauris at metus cursus condimentum. Maecenas tristique odio nunc, vestibulum consequat ante auctor eu. Morbi id posuere orci. Quisque tempus lacinia nibh eget molestie. Pellentesque tristique bibendum consectetur. Pellentesque aliquet aliquet porttitor. Suspendisse interdum lacus sit amet velit egestas pellentesque.",
            likes = 0,
            dislikes = 0,
        )

        val ownCreatedComparableReviewView1 = TestComparableReviewView(
            reviewerUsername = "jack-hoffman",
            title = "Lorem ipsum sit amet",
            content = "Cras lacinia ante at eros efficitur feugiat. In dapibus ut augue non laoreet.",
            likes = 0,
            dislikes = 0
        )

        val ownCreatedComparableReviewView1Updated =
            ownCreatedComparableReviewView1.copy(content = "Donec eget justo molestie, suscipit dolor sed, varius ex.")
    }

    @Test
    @Sql("/test-mock/users.sql", "/test-mock/medias.sql", "/test-mock/reviews.sql", "/test-mock/reviews_likes.sql")
    fun `get paged reviews for dune - success`() {

        val duneMediaId = mediaRepository.getIdByTitleOrNull("Dune: Part One")!!

        val pagedReviewsResult = reviewService.getPagedReviews(duneMediaId, 0)
        assertInstanceOf<Success<List<Review>>>(pagedReviewsResult)
        val pagedReviews = pagedReviewsResult.value.map { TestComparableReviewView.fromReview(it) }
        assertAll(
            { assertEquals(5, pagedReviews.size) },
            { assertContains(pagedReviews, testComparableReviewView6) },
            { assertContains(pagedReviews, testComparableReviewView5) },
            { assertContains(pagedReviews, testComparableReviewView4) },
            { assertContains(pagedReviews, testComparableReviewView3) },
            { assertContains(pagedReviews, testComparableReviewView2) },
        )

        val secondPageOfReviewsResult = reviewService.getPagedReviews(duneMediaId, 1)
        assertInstanceOf<Success<List<Review>>>(secondPageOfReviewsResult)
        val secondPageOfReviews = secondPageOfReviewsResult.value.map { TestComparableReviewView.fromReview(it) }
        assertAll(
            { assertEquals(1, secondPageOfReviews.size) },
            { assertContains(secondPageOfReviews, testComparableReviewView1) }
        )
    }

    @Test
    @Sql("/test-mock/users.sql", "/test-mock/medias.sql", "/test-mock/reviews.sql", "/test-mock/reviews_likes.sql")
    fun `get paged reviews for non-existent - not found`() {
        val pagedReviews = reviewService.getPagedReviews(-1, 0)
        assertInstanceOf<Failure<ErrorFindingPagedReviews>>(pagedReviews)
    }

    @Test
    @Sql("/test-mock/users.sql", "/test-mock/medias.sql", "/test-mock/reviews.sql", "/test-mock/reviews_likes.sql")
    fun `create and update review for dune - success`() {
        val duneMediaId = mediaRepository.getIdByTitleOrNull("Dune: Part One")!!

        val possibleReviewCreatingError = reviewService.saveReviewOrError(
            mediaId = duneMediaId,
            creatorUsername = "jack-hoffman",
            reviewContent = ReviewContent(
                title = "Lorem ipsum sit amet",
                content = "Cras lacinia ante at eros efficitur feugiat. In dapibus ut augue non laoreet.",
                spoilerFree = true
            )
        )

        assertNull(possibleReviewCreatingError)

        val review = reviewService.getReview(mediaId = duneMediaId, creatorUsername = "jack-hoffman")
        assertNotNull(review)

        assertEquals(ownCreatedComparableReviewView1, TestComparableReviewView.fromReview(review))

        val possibleReviewUpdatingError = reviewService.saveReviewOrError(
            mediaId = duneMediaId,
            creatorUsername = "jack-hoffman",
            reviewContent = ReviewContent(
                title = "Lorem ipsum sit amet",
                content = "Donec eget justo molestie, suscipit dolor sed, varius ex.",
                spoilerFree = true
            )
        )

        assertNull(possibleReviewUpdatingError)

        val reviewModified = reviewService.getReview(mediaId = duneMediaId, creatorUsername = "jack-hoffman")
        assertNotNull(reviewModified)

        assertEquals(ownCreatedComparableReviewView1Updated, TestComparableReviewView.fromReview(reviewModified))
    }

}