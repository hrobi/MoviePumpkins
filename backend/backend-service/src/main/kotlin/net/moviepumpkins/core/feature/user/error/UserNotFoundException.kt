package net.moviepumpkins.core.feature.user.error

import net.moviepumpkins.core.shared.error.ResourceNotFoundException

class UserNotFoundException(val username: String) : ResourceNotFoundException()