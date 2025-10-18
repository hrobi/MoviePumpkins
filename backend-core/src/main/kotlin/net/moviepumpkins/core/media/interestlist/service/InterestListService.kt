package net.moviepumpkins.core.media.interestlist.service

import jakarta.transaction.Transactional
import net.moviepumpkins.core.app.config.InterestListProperties
import net.moviepumpkins.core.media.interestlist.entity.InterestListEntity
import net.moviepumpkins.core.media.interestlist.entity.InterestListRepository
import net.moviepumpkins.core.media.interestlist.mapping.toInterestListItem
import net.moviepumpkins.core.media.interestlist.model.InterestListItem
import net.moviepumpkins.core.media.mediadetails.entity.MediaEntity
import net.moviepumpkins.core.media.mediadetails.entity.MediaRepository
import net.moviepumpkins.core.user.entity.UserAccountEntity
import net.moviepumpkins.core.user.repository.UserAccountRepository
import net.moviepumpkins.core.util.result.Failure
import net.moviepumpkins.core.util.result.Result
import net.moviepumpkins.core.util.result.Success
import net.moviepumpkins.core.util.result.succeedOrElse
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Component

@Component
class InterestListService(
    private val interestListRepository: InterestListRepository,
    private val userAccountRepository: UserAccountRepository,
    private val mediaRepository: MediaRepository,
    private val interestListProperties: InterestListProperties,
) {

    sealed interface ErrorAddingMediaToList
    sealed interface ErrorGettingInterestListPaged

    data object MediaDoesNotExistError : ErrorAddingMediaToList, ErrorGettingInterestListPaged
    data object InterestListIdDoesNotExist


    data class References(val userReference: UserAccountEntity, val mediaReference: MediaEntity)

    @Transactional
    fun getReferencesForInterestList(
        username: String,
        mediaId: Long,
    ): Result<References, MediaDoesNotExistError> {
        if (mediaRepository.existsById(mediaId)) {
            return Failure(MediaDoesNotExistError)
        }

        val mediaReference = mediaRepository.getReferenceById(mediaId)
        val userReference = userAccountRepository.getReferenceById(username)

        return Success(References(userReference, mediaReference))
    }

    @Transactional
    fun saveMedia(username: String, mediaId: Long): Result<Unit, ErrorAddingMediaToList> {
        val (userReference, mediaReference) = getReferencesForInterestList(
            username,
            mediaId
        ).succeedOrElse { return this }

        interestListRepository.save(InterestListEntity(user = userReference, media = mediaReference))
        return Success(Unit)
    }

    @Transactional
    fun getInterestListPaged(
        username: String,
        mediaId: Long,
        page: Int,
    ): Result<List<InterestListItem>, ErrorGettingInterestListPaged> {
        val (userReference, mediaReference) = getReferencesForInterestList(
            username,
            mediaId
        ).succeedOrElse { return this }

        val interestListItemsPaged = interestListRepository.findByUserAndMedia(
            userReference,
            mediaReference,
            PageRequest.of(
                page,
                interestListProperties.pageSize,
                Sort.by(InterestListEntity::modifiedAt.name).descending()
            )
        ).map {
            it.toInterestListItem()
        }

        return Success(interestListItemsPaged)
    }

    @Transactional
    fun removeInterestListItem(itemId: Long): Result<Unit, InterestListIdDoesNotExist> {
        if (!interestListRepository.existsById(itemId)) {
            return Failure(InterestListIdDoesNotExist)
        }

        interestListRepository.deleteById(itemId)
        return Success(Unit)
    }
}