package net.moviepumpkins.core.application.fakedata.disposablereviews

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface DisposableReviewRepository : JpaRepository<DisposableReview, Long> {

    @Query("select dr.id from DisposableReview dr")
    fun findAllIds(): List<Long>

}