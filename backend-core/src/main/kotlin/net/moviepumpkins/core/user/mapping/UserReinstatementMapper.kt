package net.moviepumpkins.core.user.mapping

import net.moviepumpkins.core.user.entity.UserReinstatementEntity
import net.moviepumpkins.core.user.model.UserReinstatement

object UserReinstatementMapper {
    fun fromUserReinstatementEntity(userReinstatementEntity: UserReinstatementEntity) = UserReinstatement(
        user = userReinstatementEntity.disabledUser.user.toUserAccount(),
        disabler = userReinstatementEntity.disabledUser.disabler.toUserAccount(),
        disablingReason = userReinstatementEntity.disabledUser.reason,
        requestReason = userReinstatementEntity.reason
    )

}