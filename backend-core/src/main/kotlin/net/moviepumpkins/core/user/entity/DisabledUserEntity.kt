package net.moviepumpkins.core.user.entity

import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import net.moviepumpkins.core.app.entity.BaseEntity
import org.springframework.data.jpa.repository.JpaRepository

interface DisabledUserRepository : JpaRepository<DisabledUserEntity, String>

@Entity
@Table(name = "user_disabling")
class DisabledUserEntity(
    @Id
    var username: String,

    @OneToOne
    var user: UserAccountEntity,

    @ManyToOne
    @JoinColumn(name = "disabler")
    var disabler: UserAccountEntity,

    var reason: String,
) : BaseEntity()