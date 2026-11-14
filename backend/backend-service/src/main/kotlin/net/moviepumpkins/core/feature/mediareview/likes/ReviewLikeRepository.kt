package net.moviepumpkins.core.feature.mediareview.likes

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface ReviewLikeRepository : JpaRepository<ReviewLike, ReviewLikeId> {

    @Query(
        """
        select reviewLike.id.reviewId
        from ReviewLike reviewLike
        where reviewLike.id.reviewId in :reviewIds
        and reviewLike.id.likerUsername = :username
        """
    )
    fun findReviewIdsLikedByUser(
        @Param("reviewIds") reviewIds: Collection<Long>,
        @Param("username") username: String,
    ): Set<Long>

}
