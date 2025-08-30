package net.moviepumpkins.core.user.entity

import jakarta.persistence.Entity
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
import jakarta.persistence.Table
import net.moviepumpkins.core.app.entity.BaseEntity
import net.moviepumpkins.core.user.model.UserRole
import net.moviepumpkins.core.util.equalsBy
import org.hibernate.annotations.DynamicUpdate
import org.hibernate.annotations.JdbcType
import org.hibernate.dialect.PostgreSQLEnumJdbcType
import org.hibernate.validator.constraints.Length
import javax.validation.constraints.Pattern

@Entity
@Table(name = "user_account")
@DynamicUpdate
class UserAccountEntity(
    @field:Id
    @Pattern(regexp = "([a-z0-9]+-?[a-z0-9]+)+")
    @Length(min = 3, max = 100)
    var username: String?,

    var fullName: String,

    @Pattern(regexp = "^[a-zA-Z0-9_.+-]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+\$")
    var email: String,

    var displayName: String,

    @field:Enumerated
    @field:JdbcType(PostgreSQLEnumJdbcType::class)
    var role: UserRole = UserRole.REVIEWER,
) : BaseEntity() {
    override fun equals(other: Any?): Boolean = equalsBy(this, other) { email }
    override fun hashCode(): Int = email.hashCode()
}