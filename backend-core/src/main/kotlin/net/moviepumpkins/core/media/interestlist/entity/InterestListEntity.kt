package net.moviepumpkins.core.media.interestlist.entity

import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import net.moviepumpkins.core.app.entity.BaseEntity
import net.moviepumpkins.core.media.mediadetails.entity.MediaEntity
import net.moviepumpkins.core.user.entity.UserAccountEntity
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface InterestListRepository : JpaRepository<InterestListEntity, Long> {

    @Query("SELECT ile FROM InterestListEntity ile JOIN FETCH ile.media WHERE ile.user = :user AND ile.media = :media")
    fun findByUserAndMedia(user: UserAccountEntity, media: MediaEntity, pageable: Pageable): List<InterestListEntity>
}

@Entity
@Table(name = "interest_list")
class InterestListEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    var id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "username")
    var user: UserAccountEntity,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "media_id")
    var media: MediaEntity,
) : BaseEntity()