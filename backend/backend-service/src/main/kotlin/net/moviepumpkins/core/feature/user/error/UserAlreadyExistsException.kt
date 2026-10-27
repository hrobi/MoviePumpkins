package net.moviepumpkins.core.feature.user.error

import net.moviepumpkins.core.shared.error.ResourceConflictException

class UserAlreadyExistsException(val username: String) : ResourceConflictException()