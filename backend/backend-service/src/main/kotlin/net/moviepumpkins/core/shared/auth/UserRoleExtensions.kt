package net.moviepumpkins.core.shared.auth

import net.moviepumpkins.core.feature.user.UserRole
import org.springframework.security.core.authority.SimpleGrantedAuthority

fun UserRole.mapToSimpleGrantedAuthority(): SimpleGrantedAuthority {
    return SimpleGrantedAuthority("ROLE_${this.name}")
}