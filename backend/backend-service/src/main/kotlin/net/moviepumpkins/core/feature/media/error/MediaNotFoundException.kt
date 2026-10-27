package net.moviepumpkins.core.feature.media.error

import net.moviepumpkins.core.shared.error.ResourceNotFoundException

class MediaNotFoundException(val mediaId: Long) : ResourceNotFoundException()
