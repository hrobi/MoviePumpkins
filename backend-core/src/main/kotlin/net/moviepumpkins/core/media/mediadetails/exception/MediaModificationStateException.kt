package net.moviepumpkins.core.media.mediadetails.exception

class MediaModificationStateException(val state: MediaModificationStateError) : RuntimeException() {
    companion object {
        fun mediaDoesNotExist() = MediaModificationStateException(MediaModificationStateError.ModificationDoesNotExist)
    }
}

sealed interface MediaModificationStateError {
    data object ModificationDoesNotExist : MediaModificationStateError
}