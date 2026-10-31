package net.moviepumpkins.core.shared.auth

import net.moviepumpkins.core.feature.user.UserAccountDetails
import net.moviepumpkins.core.feature.user.UserRole
import org.springframework.security.oauth2.jwt.Jwt

object JwtMapper {
    fun mapJwtToUserAccount(jwt: Jwt, userRole: UserRole): UserAccountDetails {
        return with(jwt) {
            UserAccountDetails(
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