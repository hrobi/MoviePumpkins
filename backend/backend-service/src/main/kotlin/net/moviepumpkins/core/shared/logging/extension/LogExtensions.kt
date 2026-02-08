package net.moviepumpkins.core.shared.logging.extension

import org.slf4j.Logger

fun Logger.lazyDebug(createMessage: () -> String) {
    if (isDebugEnabled) {
        this.debug(createMessage())
    }
}