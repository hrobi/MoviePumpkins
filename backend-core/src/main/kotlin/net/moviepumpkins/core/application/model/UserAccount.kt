package net.moviepumpkins.core.application.model

import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority

data class UserAccount(
    val username: String,
    val role: UserRole = UserRole.REVIEWER,
) : Authentication {

    override fun getName(): String = username

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> =
        listOf(SimpleGrantedAuthority("ROLE_${role.name}")).toMutableList()

    override fun getCredentials(): Any {
        TODO("Not yet implemented")
    }

    override fun getDetails(): Any {
        TODO("Not yet implemented")
    }

    override fun getPrincipal(): Any = this

    override fun isAuthenticated(): Boolean = true

    override fun setAuthenticated(isAuthenticated: Boolean) {
        TODO("Not yet implemented")
    }
}