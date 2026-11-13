package net.moviepumpkins.core.feature.mediareview

import net.moviepumpkins.core.feature.media.Media
import net.moviepumpkins.core.feature.user.UserAccount
import net.moviepumpkins.core.shared.entity.RepositoryBase
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface ReviewRepository : RepositoryBase<Review> {

    @Query(
        """
        select review
        from Review review
        join fetch review.reviewer
        where review.id = :reviewId
        and review.media.id = :mediaId
    """
    )
    fun findByMediaIdAndReviewIdWithReviewer(
        @Param("mediaId") mediaId: Long,
        @Param("reviewId") reviewId: Long
    ): Review?

    @Query(
        """
        select r
        from Review r
        join fetch r.reviewer
        where r.media.id = :mediaId
        order by r.createdAt desc, r.id desc
        """,
        countQuery = """
        select count(review.id)
        from Review review
        join review.media media
        where media.id = :mediaId
        """,
    )
    fun findPageByMediaId(
        @Param("mediaId") mediaId: Long,
        @Param("currentUsername") currentUsername: String?,
        pageable: Pageable
    ): Page<Review>

    @Query(
        """
        select r
        from Review r
        join fetch r.reviewer
        where r.id = :reviewId
        and r.media.id = :mediaId
    """
    )
    fun findByMediaIdAndReviewId(
        @Param("mediaId") mediaId: Long,
        @Param("reviewId") reviewId: Long,
        @Param("currentUsername") currentUsername: String?
    ): Review?

    @Query(
        """
        select r
        from Review r
        join fetch r.reviewer
        where r.media.id = :mediaId
        and r.reviewer.username = :username
    """
    )
    fun findViewByReviewerUsernameAndMediaId(username: String, mediaId: Long): Review?

    fun existsByReviewerAndMedia(reviewer: UserAccount, media: Media): Boolean

}
