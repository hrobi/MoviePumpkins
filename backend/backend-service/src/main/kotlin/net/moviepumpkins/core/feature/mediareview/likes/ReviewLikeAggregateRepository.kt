package net.moviepumpkins.core.feature.mediareview.likes

import org.springframework.data.jpa.repository.JpaRepository

interface ReviewLikeAggregateRepository : JpaRepository<ReviewLikeAggregate, Long>