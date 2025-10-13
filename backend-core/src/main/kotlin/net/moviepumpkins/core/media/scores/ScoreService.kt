package net.moviepumpkins.core.media.scores

import jakarta.transaction.Transactional
import net.moviepumpkins.core.app.config.PagedProperties
import net.moviepumpkins.core.app.config.ScoringProperties
import net.moviepumpkins.core.flavour.model.MediaFlavour
import net.moviepumpkins.core.media.mediadetails.entity.MediaRepository
import net.moviepumpkins.core.media.scores.entity.MediaFlavourRepository
import net.moviepumpkins.core.media.scores.entity.MediaRatingAggregateRepository
import net.moviepumpkins.core.media.scores.entity.MediaRatingAggregateView
import net.moviepumpkins.core.media.scores.entity.MediaRatingEntity
import net.moviepumpkins.core.media.scores.entity.MediaRatingRepository
import net.moviepumpkins.core.media.scores.mapping.toMediaScore
import net.moviepumpkins.core.media.scores.model.ErrorSavingMediaScore
import net.moviepumpkins.core.media.scores.model.FlavourDoesNotExistError
import net.moviepumpkins.core.media.scores.model.InvalidScoreError
import net.moviepumpkins.core.media.scores.model.MediaDoesNotExistError
import net.moviepumpkins.core.media.scores.model.MediaScore
import net.moviepumpkins.core.user.repository.UserAccountRepository
import net.moviepumpkins.core.util.result.Failure
import net.moviepumpkins.core.util.result.Result
import net.moviepumpkins.core.util.result.Success
import net.moviepumpkins.core.util.shortcircuit.trueOrElse
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Component

@Component
class ScoreService(
    private val mediaFlavourRepository: MediaFlavourRepository,
    private val mediaRepository: MediaRepository,
    private val mediaRatingRepository: MediaRatingRepository,
    private val mediaRatingAggregateRepository: MediaRatingAggregateRepository,
    private val userAccountRepository: UserAccountRepository,
    private val scoringProperties: ScoringProperties,
    private val pagedProperties: PagedProperties,
) {
    @Transactional
    fun getAllFlavours(): Set<MediaFlavour> {
        return mediaFlavourRepository.findAll().map {
            MediaFlavour(
                id = it.id!!,
                name = it.flavourName,
                description = it.shortDescription
            )
        }.toSet()
    }

    @Transactional
    fun saveMediaScoreOrError(
        mediaId: Long,
        flavourId: String,
        score: Float,
        scoreGiverUsername: String,
    ): ErrorSavingMediaScore? {
        if (!mediaRepository.existsById(mediaId)) {
            return MediaDoesNotExistError
        }

        if (!mediaFlavourRepository.existsById(flavourId)) {
            return FlavourDoesNotExistError
        }

        val (minScore, maxScore) = scoringProperties

        if (score != 0.0f && (score < minScore || score > maxScore)) {
            return InvalidScoreError(minScore, maxScore)
        }

        val userReference = userAccountRepository.getReferenceById(scoreGiverUsername)
        val mediaReference = mediaRepository.getReferenceById(mediaId)
        val flavourReference = mediaFlavourRepository.getReferenceById(flavourId)

        val ratingIdOrNull = mediaRatingRepository.getIdByUserAndMediaAndFlavourOrNull(
            userReference,
            mediaReference,
            flavourReference
        )

        if (score == 0.0f && ratingIdOrNull != null) {
            mediaRatingRepository.deleteById(ratingIdOrNull)
            return null
        }

        mediaRatingRepository.save(
            MediaRatingEntity(
                id = ratingIdOrNull,
                userReference,
                mediaReference,
                flavourReference,
                score = score
            )
        )

        return null
    }

    @Transactional
    fun getMediaScorePaged(mediaId: Long, page: Int): Result<List<MediaScore>, MediaDoesNotExistError> {
        if (!mediaRepository.existsById(mediaId)) {
            return Failure(MediaDoesNotExistError)
        }
        val mediaRatingAggregateViews = mediaRatingAggregateRepository.findByMediaId(
            mediaId,
            PageRequest.of(
                page,
                pagedProperties.pageSize,
                Sort.by(MediaRatingAggregateView::raterCount.name).descending()
            )
        )

        return Success(mediaRatingAggregateViews.map(MediaRatingAggregateView::toMediaScore))
    }

    @Transactional
    fun getMediaScorePagedByUser(
        username: String,
        mediaId: Long,
        page: Int,
    ): Result<List<MediaScore>, MediaDoesNotExistError> {
        if (!mediaRepository.existsById(mediaId)) {
            return Failure(MediaDoesNotExistError)
        }

        if (!userAccountRepository.existsById(username)) {
            return Success(emptyList())
        }

        val mediaRatings = mediaRatingRepository.findByUserAndMedia(
            userAccountRepository.getReferenceById(username),
            mediaRepository.getReferenceById(mediaId),
            PageRequest.of(page, pagedProperties.pageSize, Sort.by(MediaRatingEntity::score.name).descending())
        )

        return Success(mediaRatings.map(MediaRatingEntity::toMediaScore))
    }

    @Transactional
    fun getMediaScoreByUsernameAndMediaIdAndFlavourId(
        username: String,
        mediaId: Long,
        flavourId: String,
    ): Result<MediaScore?, MediaDoesNotExistError> {
        mediaRepository.existsById(mediaId).trueOrElse { return Failure(MediaDoesNotExistError) }
        if (
            !userAccountRepository.existsById(username) ||
            !mediaFlavourRepository.existsById(flavourId)
        ) {
            return Success(null)
        }

        val rating = mediaRatingRepository.findByUserAndMediaAndFlavour(
            userAccountRepository.getReferenceById(username),
            mediaRepository.getReferenceById(mediaId),
            mediaFlavourRepository.getReferenceById(flavourId)
        )

        return Success(rating?.toMediaScore())
    }

    @Transactional
    fun countScoredFlavoursForMediaIfExists(mediaId: Long): Int? {
        mediaRepository.existsById(mediaId).trueOrElse { return null }
        return mediaRatingAggregateRepository.countByMediaId(mediaId)
    }

    @Transactional
    fun countScoredFlavoursForMediaAndUserIfExists(mediaId: Long, username: String): Int? {
        mediaRepository.existsById(mediaId).trueOrElse { return null }
        return mediaRatingRepository.countByMediaAndUser(
            mediaRepository.getReferenceById(mediaId),
            userAccountRepository.getReferenceById(username)
        )
    }
}