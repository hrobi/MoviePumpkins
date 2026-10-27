package net.moviepumpkins.core.shared

inline fun <reified T> className() = T::class.java.simpleName