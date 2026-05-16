package net.moviepumpkins.core.feature.media.domain.model.value

enum class MPARating {
    G,
    PG,
    PG_13,
    R,
    NC_17;

    companion object {
        fun parseOrNull(rating: String): MPARating? {
            val formattedRatingString = rating.uppercase().replace("-", "_")
            return when (formattedRatingString) {
                G.name -> G
                PG.name -> PG
                PG_13.name -> PG_13
                R.name -> R
                NC_17.name -> NC_17
                "TV_14" -> PG_13
                else -> null
            }
        }
    }
}