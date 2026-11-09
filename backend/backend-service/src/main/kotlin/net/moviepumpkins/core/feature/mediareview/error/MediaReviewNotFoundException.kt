package net.moviepumpkins.core.feature.mediareview.error

import net.moviepumpkins.core.shared.error.ResourceNotFoundException

class MediaReviewNotFoundException(val reviewId: Long, val mediaId: Long) : ResourceNotFoundException()