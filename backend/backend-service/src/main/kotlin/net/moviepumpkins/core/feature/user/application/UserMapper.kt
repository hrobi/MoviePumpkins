package net.moviepumpkins.core.feature.user.application

import net.moviepumpkins.core.api.models.UserDto
import net.moviepumpkins.core.feature.user.UserAccount

object UserMapper {

    fun toResponse(projection: UserAccount.View): UserDto = with(projection) {
        UserDto(
            username = username,
            displayName = displayName,
            firstName = firstName,
            lastName = lastName,
            email = email,
            role = UserRoleMapper.toResponse(role),
            enabled = enabled,
        )
    }

}