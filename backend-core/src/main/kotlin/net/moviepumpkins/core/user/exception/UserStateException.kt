package net.moviepumpkins.core.user.exception

class UserStateException(val state: UserStateError) : IllegalStateException()
sealed interface UserStateError {
    data object UserAlreadyEnabled : UserStateError
    data class UserDoesNotExist(val username: String) : UserStateError
    data object UserCannotDisableThemselves : UserStateError
}