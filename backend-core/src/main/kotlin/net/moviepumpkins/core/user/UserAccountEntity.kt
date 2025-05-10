package net.moviepumpkins.core.user

import jakarta.persistence.*
import net.moviepumpkins.core.general.equalsBy
import org.hibernate.annotations.JdbcType
import org.hibernate.annotations.NaturalId
import org.hibernate.dialect.PostgreSQLEnumJdbcType
import java.util.*


enum class UserRole {
    REVIEWER,
    SUPERVISOR,
    ADMIN
}

@Entity(name = "user_account")
class UserAccountEntity(
    @field:Id
    @field:GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    var fullName: String,
    @NaturalId
    var email: String,
    @NaturalId
    var username: String,
    var about: String = "",
    @field:Enumerated
    @field:JdbcType(PostgreSQLEnumJdbcType::class)
    var role: UserRole = UserRole.REVIEWER,
) {
    override fun equals(other: Any?): Boolean = equalsBy(this, other) { email }
    override fun hashCode(): Int = Objects.hash(email)
}
