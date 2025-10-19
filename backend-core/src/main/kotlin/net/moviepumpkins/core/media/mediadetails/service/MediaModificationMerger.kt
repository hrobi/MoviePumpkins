package net.moviepumpkins.core.media.mediadetails.service

import net.moviepumpkins.core.media.mediadetails.entity.MediaEntity
import net.moviepumpkins.core.media.mediadetails.entity.MediaModificationEntity

class MediaModificationMerger(val mediaEntity: MediaEntity, val modificationEntity: MediaModificationEntity) {
    private fun mergeNullableInt(original: Int?, mod: Int?): Int? {
        return when (mod) {
            null -> original
            -1 -> null
            else -> mod
        }
    }

    private fun mergeInt(original: Int, mod: Int?): Int {
        return when (mod) {
            null -> original
            else -> mod
        }
    }

    private fun mergeNullableString(original: String?, mod: String?): String? {
        return when (mod) {
            null -> original
            "" -> null
            else -> mod
        }
    }

    private fun mergeString(original: String, mod: String?): String {
        return when (mod) {
            null -> original
            else -> mod
        }
    }

    private fun mergeNullableStringList(original: List<String>?, mod: List<String>?): List<String>? {
        return when {
            mod == null -> original
            mod.isEmpty() -> null
            else -> mod
        }
    }

    fun merge() {
        with(mediaEntity) {
            title = mergeString(title, modificationEntity.title)
            shortDescription = mergeString(shortDescription, modificationEntity.shortDescription)
            releaseYear = mergeInt(releaseYear, modificationEntity.releaseYear)
            directors = mergeNullableStringList(directors, modificationEntity.directors)
            writers = mergeNullableStringList(writers, modificationEntity.writers)
            actors = mergeNullableStringList(actors, modificationEntity.actors)
            originalTitle = mergeNullableString(originalTitle, modificationEntity.originalTitle)
            countries = mergeNullableStringList(countries, modificationEntity.countries)
            awards = mergeNullableStringList(awards, modificationEntity.awards)
            awardsWinCount = mergeNullableInt(awardsWinCount, modificationEntity.awardsWinCount)
            nominationsCount = mergeNullableInt(nominationsCount, modificationEntity.nominationsCount)
            posterFile = mergeNullableString(posterFile, modificationEntity.posterFile)
        }
    }
}