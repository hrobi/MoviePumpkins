package net.moviepumpkins.core.app.config

import net.moviepumpkins.core.app.model.UserAccount
import net.moviepumpkins.core.app.model.UserRole
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.stereotype.Component

private const val ROLE_PREFIX = "ROLE_"

@Component
class AuthenticationFacade {
    fun extractUserAccountFromJwt(): UserAccount {
        val authentication = SecurityContextHolder.getContext().authentication
        val jwt = authentication.principal as Jwt
        val roles = jwt.getClaimAsMap("realm_access")["roles"] as List<String>
        val userIsAlreadyStored = roles.contains("app_user")
        return UserAccount(
            username = jwt.getClaimAsString("preferred_username"),
            email = jwt.getClaimAsString("email"),
            displayName = jwt.getClaimAsString("display_name"),
            fullName = jwt.getClaimAsString("name"),
            role = authentication.authorities.findFirstRole()!!.toUserRole(),
            isAppUser = userIsAlreadyStored
        )
    }

    val isStoredUser
        get() = ((SecurityContextHolder.getContext().authentication.principal as Jwt).getClaimAsMap("realm_access")["roles"] as List<String>).contains(
            "app_user"
        )

    val authenticationName
        get() = SecurityContextHolder.getContext().authentication.name

    private fun Collection<GrantedAuthority>.findFirstRole() =
        find { grantedAuthority -> grantedAuthority.authority.startsWith("ROLE_") }

    private fun GrantedAuthority.toUserRole() = UserRole.valueOf(authority.substring(ROLE_PREFIX.length))
}