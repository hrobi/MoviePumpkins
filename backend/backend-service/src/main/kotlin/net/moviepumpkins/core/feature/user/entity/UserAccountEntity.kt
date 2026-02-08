package net.moviepumpkins.core.feature.user.entity

import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
import jakarta.persistence.Table
import net.moviepumpkins.core.shared.db.util.entityEqualsBy
import net.moviepumpkins.core.feature.user.model.UserRole
import org.hibernate.annotations.DynamicUpdate
import org.hibernate.validator.constraints.Length
import javax.validation.constraints.Pattern

@Entity
@Table(name = "user_account")
@DynamicUpdate
class UserAccountEntity(
    @field:Id
    @field:Pattern(regexp = "([a-z0-9]+-?[a-z0-9]+)+")
    @field:Length(min = 3, max = 100)
    var username: String?,

    var firstName: String,

    var lastName: String,

    @field:Pattern(regexp = "^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+\$")
    var email: String,

    var displayName: String,

    @field:Enumerated(EnumType.STRING)
    var role: UserRole = UserRole.REVIEWER,

    var enabled: Boolean = true,
) {
    override fun equals(other: Any?): Boolean = entityEqualsBy(this, other) { email }
    override fun hashCode(): Int = email.hashCode()
}