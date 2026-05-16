package net.moviepumpkins.core.feature.media.domain.mapper

private const val UNKNOWN_DATA = "N/A"

object OmdbMediaDtoPropertyParser {

    fun handleNA(value: String): String? = value.takeIf { it != UNKNOWN_DATA }

    fun extractWinsAndNominations(ratingString: String): Pair<Int?, Int?> {
        if (ratingString == UNKNOWN_DATA) {
            return null to null
        }

        val totalRatingsString = ratingString
            .split(". ")
            .last()

        val winsAndNominationsRegex =
            Regex("""(?:(\d+) wins)?(?:\s*&\s*)?(?:(\d+) nominations)?""")

        val match = winsAndNominationsRegex.find(totalRatingsString)
        val wins = match?.groups[1]?.value?.toInt()
        val nominations = match?.groups[2]?.value?.toInt()

        return wins to nominations
    }

    fun extractReleaseYearAndEndYear(yearString: String): Pair<Int?, Int?> {
        return when {
            yearString == UNKNOWN_DATA -> null to null
            yearString.contains("–") -> yearString.split("–").let { it[0].toInt() to it[1].toInt() }
            else -> yearString.toInt() to null
        }
    }

    fun extractRuntimeInMinutes(runtimeString: String): Int? =
        handleNA(runtimeString)?.split(" min")[0]?.toInt()

}