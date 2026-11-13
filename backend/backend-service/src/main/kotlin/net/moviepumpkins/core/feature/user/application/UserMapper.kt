package net.moviepumpkins.core.feature.user.application

import net.moviepumpkins.core.api.models.UserDto
import net.moviepumpkins.core.feature.user.UserAccount

object UserMapper {

    fun toResponse(entity: UserAccount): UserDto = with(entity) {
        UserDto(
            username = username!!,
            displayName = displayName,
            firstName = firstName,
            lastName = lastName,
            email = email,
            role = UserRoleMapper.toResponse(role),
            enabled = enabled,
        )
    }

}