package net.moviepumpkins.core.media.scores

import io.konform.validation.Validation
import io.konform.validation.constraints.maximum
import io.konform.validation.constraints.minimum
import jakarta.transaction.Transactional
import net.moviepumpkins.core.app.exception.throwIfInvalid
import net.moviepumpkins.core.flavour.model.MediaFlavour
import net.moviepumpkins.core.media.mediadetails.entity.MediaRepository
import net.moviepumpkins.core.media.scores.entity.MediaFlavourRepository
import net.moviepumpkins.core.media.scores.entity.MediaRatingAggregateRepository
import net.moviepumpkins.core.media.scores.entity.MediaRatingAggregateView
import net.moviepumpkins.core.media.scores.entity.MediaRatingEntity
import net.moviepumpkins.core.media.scores.entity.MediaRatingRepository
import net.moviepumpkins.core.media.scores.exception.ScoringStateException
import net.moviepumpkins.core.media.scores.mapping.toMediaScore
import net.moviepumpkins.core.media.scores.model.MediaScore
import net.moviepumpkins.core.user.repository.UserAccountRepository
import net.moviepumpkins.core.util.jpa.getReferenceByIdOrThrow
import net.moviepumpkins.core.util.shortcircuit.trueOrElse
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Component

private const val MIN_SCORE = 1
private const val MAX_SCORE = 5
private const val PAGE_SIZE = 12

@Component
class ScoreService(
    private val mediaFlavourRepository: MediaFlavourRepository,
    private val mediaRepository: MediaRepository,
    private val mediaRatingRepository: MediaRatingRepository,
    private val mediaRatingAggregateRepository: MediaRatingAggregateRepository,
    private val userAccountRepository: UserAccountRepository,
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
    fun saveMediaScore(
        mediaId: Long,
        flavourId: String,
        score: Float,
        scoreGiverUsername: String,
    ) {

        val userReference =
            userAccountRepository.getReferenceByIdOrThrow(scoreGiverUsername) { ScoringStateException.userDoesNotExist() }
        val mediaReference =
            mediaRepository.getReferenceByIdOrThrow(mediaId) { ScoringStateException.mediaDoesNotExist() }

        Validation<Float> {
            dynamic {
                if (it != 0.0f) {
                    minimum(MIN_SCORE)
                    maximum(MAX_SCORE)
                }
            }
        }.validate(score).throwIfInvalid()

        val flavourReference =
            mediaFlavourRepository.getReferenceByIdOrThrow(flavourId) { ScoringStateException.flavourDoesNotExist() }

        val ratingIdOrNull = mediaRatingRepository.getIdByUserAndMediaAndFlavourOrNull(
            userReference,
            mediaReference,
            flavourReference
        )

        if (score == 0.0f && ratingIdOrNull != null) {
            mediaRatingRepository.deleteById(ratingIdOrNull)
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
    }

    @Transactional
    fun getMediaScorePaged(mediaId: Long, page: Int): List<MediaScore> {
        if (!mediaRepository.existsById(mediaId)) {
            throw ScoringStateException.mediaDoesNotExist()
        }
        val mediaRatingAggregateViews = mediaRatingAggregateRepository.findByMediaId(
            mediaId,
            PageRequest.of(
                page,
                PAGE_SIZE,
                Sort.by(MediaRatingAggregateView::raterCount.name).descending()
            )
        )

        return mediaRatingAggregateViews.map(MediaRatingAggregateView::toMediaScore)
    }

    @Transactional
    fun getMediaScorePagedByUser(
        username: String,
        mediaId: Long,
        page: Int,
    ): List<MediaScore> {

        val mediaRatings = mediaRatingRepository.findByUserAndMedia(
            userAccountRepository.getReferenceByIdOrThrow(username) { ScoringStateException.userDoesNotExist() },
            mediaRepository.getReferenceByIdOrThrow(mediaId) { ScoringStateException.mediaDoesNotExist() },
            PageRequest.of(page, PAGE_SIZE, Sort.by(MediaRatingEntity::score.name).descending())
        )

        return mediaRatings.map(MediaRatingEntity::toMediaScore)
    }

    @Transactional
    fun getMediaScoreByUsernameAndMediaIdAndFlavourId(
        username: String,
        mediaId: Long,
        flavourId: String,
    ): MediaScore? {

        val rating = mediaRatingRepository.findByUserAndMediaAndFlavour(
            userAccountRepository.getReferenceByIdOrThrow(username) { throw ScoringStateException.userDoesNotExist() },
            mediaRepository.getReferenceByIdOrThrow(mediaId) { throw ScoringStateException.mediaDoesNotExist() },
            mediaFlavourRepository.getReferenceByIdOrThrow(flavourId) { throw ScoringStateException.flavourDoesNotExist() }
        )

        return rating?.toMediaScore()
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