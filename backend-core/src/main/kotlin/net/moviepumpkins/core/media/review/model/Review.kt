package net.moviepumpkins.core.media.review.model

import net.moviepumpkins.core.app.model.UserAccount
import java.time.OffsetDateTime

open class ReviewContent(
    open val title: String,
    open val content: String,
    open val spoilerFree: Boolean,
)

data class Review(
    val id: Long,
    override val title: String,
    override val content: String,
    override val spoilerFree: Boolean,
    val likes: Int,
    val dislikes: Int,
    val createdAt: OffsetDateTime,
    val modifiedAt: OffsetDateTime,
    val creator: UserAccount,
    val userOwnRating: ReviewRatingType? = null,
) : ReviewContent(title, content, spoilerFree)