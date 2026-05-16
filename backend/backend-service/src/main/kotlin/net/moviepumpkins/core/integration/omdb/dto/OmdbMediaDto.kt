package net.moviepumpkins.core.integration.omdb.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class OmdbMediaDto(
    @JsonProperty("Title") val title: String,
    @JsonProperty("Year") val year: String,
    @JsonProperty("Rated") val rated: String,
    @JsonProperty("Released") val released: String,
    @JsonProperty("Runtime") val runtime: String,
    @JsonProperty("Genre") val genre: String,
    @JsonProperty("Director") val director: String,
    @JsonProperty("Writer") val writer: String,
    @JsonProperty("Actors") val actors: String,
    @JsonProperty("Plot") val plot: String,
    @JsonProperty("Language") val language: String,
    @JsonProperty("Country") val country: String,
    @JsonProperty("Awards") val awards: String,
    @JsonProperty("Poster") val poster: String,
    @JsonProperty("Ratings") val ratings: List<OmdbRatingDto>,
    @JsonProperty("Metascore") val metascore: String,
    @JsonProperty("imdbRating") val imdbRating: String,
    @JsonProperty("imdbVotes") val imdbVotes: String,
    @JsonProperty("imdbID") val imdbId: String,
    @JsonProperty("Type") val type: String,
    @JsonProperty("totalSeasons") val totalSeasons: String?,
    @JsonProperty("Response") val response: String,
)
