package net.moviepumpkins.core.util.shortcircuit

inline fun <T> List<T>.emptyOrElse(action: List<T>.() -> Nothing) {
    if (this.isEmpty()) {
        return
    }
    action()
}

inline fun Boolean.trueOrElse(action: () -> Nothing) {
    if (!this) {
        action()
    }
}