package net.moviepumpkins.core.shared.api.mapper

import net.moviepumpkins.core.feature.user.model.UserAccount
import net.moviepumpkins.core.feature.user.model.UserRole
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.stereotype.Component

@Component
class JwtMapper {
    fun mapJwtToUserAccount(jwt: Jwt, userRole: UserRole): UserAccount {
        return with(jwt) {
            UserAccount(
                username = getClaimAsString("preferred_username"),
                email = getClaimAsString("email"),
                displayName = getClaimAsString("display_name"),
                firstName = getClaimAsString("first_name"),
                lastName = getClaimAsString("last_name"),
                role = userRole,
                enabled = true,
            )
        }
    }
}