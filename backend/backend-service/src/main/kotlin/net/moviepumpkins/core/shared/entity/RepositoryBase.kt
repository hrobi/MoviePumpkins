package net.moviepumpkins.core.shared.entity

import org.springframework.data.jpa.repository.JpaRepository

interface RepositoryBase<T : EntityBase> : JpaRepository<T, Long> {

    fun find(id: Long): T?

}