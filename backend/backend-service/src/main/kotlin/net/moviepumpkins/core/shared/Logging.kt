package net.moviepumpkins.core.shared

import org.slf4j.Logger
import org.slf4j.LoggerFactory

fun Any.logger(): Logger = LoggerFactory.getLogger(this::class.java)

fun Logger.lazyDebug(createMessage: () -> String) {
    if (isDebugEnabled) {
        this.debug(createMessage())
    }
}