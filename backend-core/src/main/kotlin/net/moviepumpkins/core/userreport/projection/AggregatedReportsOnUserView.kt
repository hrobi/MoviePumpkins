package net.moviepumpkins.core.userreport.projection

import net.moviepumpkins.core.media.review.entity.ReviewEntity

interface AggregatedReportsOnUserView {
    val review: ReviewEntity
    val reportCount: Int
}