package net.moviepumpkins.core.user.exception

import net.moviepumpkins.core.app.exception.BasicClientErrorException

class UserNotFoundByUsernameExceptionBasic(val username: String) : BasicClientErrorException(
    reason = "User with username $username couldn't be found",
    status = 404,
)