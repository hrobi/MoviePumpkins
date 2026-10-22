package net.moviepumpkins.core.feature.user.application

import net.moviepumpkins.core.api.models.UserDto
import net.moviepumpkins.core.api.models.UserRoleEnum
import net.moviepumpkins.core.feature.user.UserAccountEntity

object UserMapper {

    fun mapToDto(entity: UserAccountEntity): UserDto {
        return UserDto(
            username = entity.username!!,
            displayName = entity.displayName,
            firstName = entity.firstName,
            lastName = entity.lastName,
            email = entity.email,
            role = UserRoleEnum.valueOf(entity.role.name),
            enabled = entity.enabled,
        )
    }

}