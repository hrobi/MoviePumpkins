package net.moviepumpkins.core.feature.mediareview.application

import net.moviepumpkins.core.api.controllers.MediaOwnReviewController
import net.moviepumpkins.core.api.controllers.MediaReviewsController
import net.moviepumpkins.core.api.controllers.MediaReviewsToggleLikeController
import net.moviepumpkins.core.api.models.OwnReviewDto
import net.moviepumpkins.core.api.models.PagedReviewDto
import net.moviepumpkins.core.api.models.ReviewDto
import net.moviepumpkins.core.api.models.ReviewLikeDto
import net.moviepumpkins.core.api.models.UpsertReviewDto
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.RestController
import java.net.URI

@RestController
@Validated
class MediaReviewController(
    private val mediaReviewService: MediaReviewService,
) : MediaReviewsController, MediaOwnReviewController, MediaReviewsToggleLikeController {

    override fun createReview(
        upsertReviewDto: UpsertReviewDto,
        mediaId: Long
    ): ResponseEntity<Unit> {
        val reviewId = mediaReviewService.createMediaReviewAndGetId(mediaId, upsertReviewDto)
        return ResponseEntity.created(URI("/media/$mediaId/reviews/$reviewId")).build()
    }

    override fun getMediaReviews(
        mediaId: Long,
        page: Int,
    ): ResponseEntity<PagedReviewDto> =
        ResponseEntity.ok(mediaReviewService.getMediaReviews(mediaId, page))

    override fun getMediaReview(
        mediaId: Long,
        reviewId: Long
    ): ResponseEntity<ReviewDto> {
        val reviewDto = mediaReviewService.getMediaReview(mediaId, reviewId)
        return ResponseEntity.ok(reviewDto)
    }

    override fun updateReview(
        upsertReviewDto: UpsertReviewDto,
        mediaId: Long,
        reviewId: Long
    ): ResponseEntity<Unit> {
        mediaReviewService.updateMediaReview(mediaId, reviewId, upsertReviewDto)
        return ResponseEntity.noContent().build()
    }

    override fun getOwnMediaReview(mediaId: Long): ResponseEntity<OwnReviewDto> {
        val ownMediaReview = mediaReviewService.getOwnMediaReview(mediaId)
        return ResponseEntity.ok(ownMediaReview)
    }

    override fun toggleMediaReviewLike(
        mediaId: Long,
        reviewId: Long
    ): ResponseEntity<ReviewLikeDto> {
        val likeDto = mediaReviewService.toggleLike(mediaId, reviewId)
        return ResponseEntity.ok(likeDto)
    }

}