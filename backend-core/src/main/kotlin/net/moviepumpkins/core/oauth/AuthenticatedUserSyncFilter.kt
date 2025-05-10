package net.moviepumpkins.core.oauth

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import net.moviepumpkins.core.user.UserAccount
import net.moviepumpkins.core.user.UserRole
import net.moviepumpkins.core.user.UserService
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.web.filter.OncePerRequestFilter

open class AuthenticatedUserSyncFilter(
    private val userService: UserService,
) : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val authToken = SecurityContextHolder.getContext().authentication as JwtAuthenticationToken
        val accessToken = authToken.token

        val realmRoles = accessToken.getClaimAsMap("realm_access")["roles"] as List<String>

        val isUserAlreadyInDatabase = realmRoles.contains("app_user")
        val userAccount = if (isUserAlreadyInDatabase) {
            userService.getUserAccountFromDatabaseByUsername(accessToken.getClaimAsString("preferred_username"))
        } else {
            val userAccount = UserAccount(
                username = accessToken.getClaimAsString("preferred_username"),
                email = accessToken.getClaimAsString("email"),
                fullName = accessToken.getClaimAsString("name"),
                role = UserRole.REVIEWER,
            )
            userService.syncUser(
                userAccount,
                sid = accessToken.subject,
                existsInDatabase = false
            )
            userAccount
        }

        SecurityContextHolder.getContext().authentication = userAccount
        filterChain.doFilter(request, response)
    }
}