package net.moviepumpkins.core.feature.mediareview.application

import net.moviepumpkins.core.api.models.ReviewDto
import net.moviepumpkins.core.api.models.UpsertReviewDto
import net.moviepumpkins.core.feature.media.Media
import net.moviepumpkins.core.feature.mediareview.Review
import net.moviepumpkins.core.feature.mediareview.ReviewChecks.contentCheck
import net.moviepumpkins.core.feature.mediareview.ReviewChecks.titleCheck
import net.moviepumpkins.core.feature.user.UserAccount
import net.moviepumpkins.core.feature.user.application.UserMapper
import net.moviepumpkins.core.shared.validation.runCheckOrThrow

object ReviewMapper {

    fun toResponse(entity: Review): ReviewDto = with(entity) {
        ReviewDto(
            id = id!!,
            title = title,
            content = content,
            reviewer = UserMapper.toResponse(reviewer),
            createdAt = createdAt,
            updatedAt = updatedAt,
        )
    }

    fun fromRequest(
        dto: UpsertReviewDto,
        reviewer: UserAccount,
        media: Media,
    ): Review {
        runCheckOrThrow(dto) {
            UpsertReviewDto::title by titleCheck
            UpsertReviewDto::content by contentCheck
        }
        return with(dto) {
            Review(
                title = title,
                content = content,
                reviewer = reviewer,
                media = media,
            )
        }
    }

    fun updateByRequest(review: Review, dto: UpsertReviewDto) {
        runCheckOrThrow(dto) {
            UpsertReviewDto::title by titleCheck
            UpsertReviewDto::content by contentCheck
        }
        review.title = dto.title
        review.content = dto.content
    }

}
