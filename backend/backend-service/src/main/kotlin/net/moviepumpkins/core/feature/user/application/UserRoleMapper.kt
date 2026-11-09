package net.moviepumpkins.core.feature.user.application

import net.moviepumpkins.core.api.models.UserRoleEnum
import net.moviepumpkins.core.feature.user.UserRole

object UserRoleMapper {

    fun toResponse(userRole: UserRole): UserRoleEnum {
        return when (userRole) {
            UserRole.REVIEWER -> UserRoleEnum.REVIEWER
            UserRole.SUPERVISOR -> UserRoleEnum.SUPERVISOR
            UserRole.ADMIN -> UserRoleEnum.ADMIN
        }
    }

}