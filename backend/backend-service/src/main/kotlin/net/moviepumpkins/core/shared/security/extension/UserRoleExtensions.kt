package net.moviepumpkins.core.shared.security.extension

import net.moviepumpkins.core.feature.user.model.UserRole
import org.springframework.security.core.authority.SimpleGrantedAuthority

fun UserRole.mapToSimpleGrantedAuthority(): SimpleGrantedAuthority {
    return SimpleGrantedAuthority("ROLE_${this.name}")
}