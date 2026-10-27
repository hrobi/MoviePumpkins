package net.moviepumpkins.core.shared

fun interface Loader<T> {

    fun load(): T

}