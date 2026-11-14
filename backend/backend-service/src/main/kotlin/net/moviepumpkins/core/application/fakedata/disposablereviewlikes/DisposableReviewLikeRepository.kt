package net.moviepumpkins.core.application.fakedata.disposablereviewlikes

import net.moviepumpkins.core.feature.mediareview.likes.ReviewLikeId
import org.springframework.data.jpa.repository.JpaRepository

interface DisposableReviewLikeRepository : JpaRepository<DisposableReviewLike, ReviewLikeId>
