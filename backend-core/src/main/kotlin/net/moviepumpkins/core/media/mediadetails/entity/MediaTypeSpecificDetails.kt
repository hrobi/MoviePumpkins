package net.moviepumpkins.core.media.mediadetails.entity

data class MediaTypeSpecificDetails(
    val lengthInMinutes: Int?,
    val seasonCount: Int?,
    val endYear: Int?,
) {

    companion object {
        fun forMovie(lengthInMinutes: Int) = MediaTypeSpecificDetails(lengthInMinutes, null, null)
        fun forSeries(seasonCount: Int, endYear: Int) = MediaTypeSpecificDetails(null, seasonCount, endYear)
    }

    fun toNullable(): MediaTypeSpecificDetails? {
        return if (listOf(lengthInMinutes, seasonCount, endYear).all { it == null }) null else this
    }
}