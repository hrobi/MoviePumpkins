package net.moviepumpkins.core.feature.media.error

import net.moviepumpkins.core.shared.error.ResourceNotFoundException
import java.util.UUID

class MediaNotFoundException(val mediaId: UUID) : ResourceNotFoundException()