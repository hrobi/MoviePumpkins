package net.moviepumpkins.core.media.mediadetails.entity

data class MediaTypeSpecificDetails(
    val lengthInMinutes: Int?,
    val seasonCount: Int?,
    val endYear: Int?,
) {
    fun toNullable(): MediaTypeSpecificDetails? {
        return if (listOf(lengthInMinutes, seasonCount, endYear).all { it == null }) null else this
    }
}