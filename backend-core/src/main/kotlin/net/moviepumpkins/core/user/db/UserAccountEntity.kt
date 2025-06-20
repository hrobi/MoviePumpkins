package net.moviepumpkins.core.user.db

import jakarta.persistence.*
import net.moviepumpkins.core.general.BaseEntity
import net.moviepumpkins.core.user.model.UserRole
import net.moviepumpkins.core.utils.equalsBy
import org.hibernate.annotations.JdbcType
import org.hibernate.annotations.NaturalId
import org.hibernate.dialect.PostgreSQLEnumJdbcType
import javax.validation.constraints.Pattern

@Entity(name = "user_account")
class UserAccountEntity(
    @field:Id
    @field:GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    var fullName: String,

    @NaturalId
    var email: String,

    @NaturalId
    @Pattern(regexp = "([a-z0-9]+-?[a-z0-9]+)+")
    var username: String,

    var displayName: String,

    var about: String = "",

    @field:Enumerated
    @field:JdbcType(PostgreSQLEnumJdbcType::class)
    var role: UserRole = UserRole.REVIEWER,
) : BaseEntity() {
    override fun equals(other: Any?): Boolean = equalsBy(this, other) { email }
    override fun hashCode(): Int = email.hashCode()
}
