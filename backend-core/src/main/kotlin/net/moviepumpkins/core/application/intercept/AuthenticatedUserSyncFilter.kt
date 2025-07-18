package net.moviepumpkins.core.application.intercept

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import net.moviepumpkins.core.application.model.UserAccount
import net.moviepumpkins.core.application.toUserAccount
import net.moviepumpkins.core.application.usecase.SyncUserUseCase
import net.moviepumpkins.core.user.UserService
import net.moviepumpkins.core.user.exception.UserNotFoundByUsernameException
import net.moviepumpkins.core.user.model.UserProfile
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.web.filter.OncePerRequestFilter
import net.moviepumpkins.core.user.model.UserRole as UserProfileRole

open class AuthenticatedUserSyncFilter(
    private val userService: UserService,
    private val syncUser: SyncUserUseCase,
) : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        if (SecurityContextHolder.getContext().authentication == null) {
            filterChain.doFilter(request, response)
            return
        }
        val authToken = SecurityContextHolder.getContext().authentication as JwtAuthenticationToken
        val accessToken = authToken.token

        val realmRoles = accessToken.getClaimAsMap("realm_access")["roles"] as List<String>

        val isUserAlreadyInDatabase = realmRoles.contains("app_user")
        val userAccount: UserAccount = if (isUserAlreadyInDatabase) {
            val username = accessToken.getClaimAsString("preferred_username")
            userService.getUserProfileByUsername(username)?.toUserAccount()
                ?: throw UserNotFoundByUsernameException(username)
        } else {
            val userProfile = accessToken.toUserProfileWithReviewerRole()
            syncUser(userProfile = userProfile, sid = accessToken.subject)
            userProfile.toUserAccount()
        }

        SecurityContextHolder.getContext().authentication = userAccount
        filterChain.doFilter(request, response)
    }

    private fun Jwt.toUserProfileWithReviewerRole() = UserProfile(
        username = getClaimAsString("preferred_username"),
        email = getClaimAsString("email"),
        displayName = getClaimAsString("display_name"),
        fullName = getClaimAsString("name"),
        role = UserProfileRole.REVIEWER
    )
}