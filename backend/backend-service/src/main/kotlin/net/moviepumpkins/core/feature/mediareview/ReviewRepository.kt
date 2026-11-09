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
        and review.parentMedia.id = :mediaId
    """
    )
    fun findByMediaIdAndReviewIdWithReviewer(
        @Param("mediaId") mediaId: Long,
        @Param("reviewId") reviewId: Long
    ): Review?

    @Query(
        """
        select r.id AS id,
               r.title AS title,
               r.content AS content,
               r.createdAt AS createdAt,
               r.updatedAt AS updatedAt,
               r.reviewer AS reviewer
        from Review r
        join fetch r.reviewer
        where r.parentMedia.id = :mediaId
        order by r.createdAt desc, r.id desc
        """,
        countQuery = """
        select count(review.id)
        from Review review
        join review.parentMedia media
        where media.id = :mediaId
        """,
    )
    fun findViewPageByMediaId(
        @Param("mediaId") mediaId: Long,
        @Param("currentUsername") currentUsername: String?,
        pageable: Pageable
    ): Page<Review.View>

    @Query(
        """
        select r.id AS id,
               r.title AS title,
               r.content AS content,
               r.createdAt AS createdAt,
               r.updatedAt AS updatedAt,
               r.reviewer AS reviewer
        from Review r
        join fetch r.reviewer
        where r.id = :reviewId
        and r.parentMedia.id = :mediaId
    """
    )
    fun findViewByMediaIdAndReviewId(
        @Param("mediaId") mediaId: Long,
        @Param("reviewId") reviewId: Long,
        @Param("currentUsername") currentUsername: String?
    ): Review.View?

    @Query(
        """
        select r.id AS id,
               r.title AS title,
               r.content AS content,
               r.createdAt AS createdAt,
               r.updatedAt AS updatedAt,
               r.reviewer AS reviewer
        from Review r
        join fetch r.reviewer
        where r.parentMedia.id = :mediaId
        and r.reviewer.username = :username
    """
    )
    fun findViewByReviewerUsernameAndMediaId(username: String, mediaId: Long): Review.View?

    fun existsByReviewerAndMedia(reviewer: UserAccount, media: Media): Boolean

}
