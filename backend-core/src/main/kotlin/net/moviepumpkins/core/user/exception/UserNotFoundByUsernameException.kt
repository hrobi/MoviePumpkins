package net.moviepumpkins.core.user.exception

import net.moviepumpkins.core.application.exception.ClientErrorException

class UserNotFoundByUsernameException(val username: String) : ClientErrorException(
    reason = "User with username $username couldn't be found",
    status = 404,
)