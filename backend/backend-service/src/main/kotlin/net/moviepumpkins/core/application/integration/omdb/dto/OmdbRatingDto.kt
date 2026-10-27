package net.moviepumpkins.core.application.integration.omdb.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class OmdbRatingDto(
    @JsonProperty("Source") val source: String,
    @JsonProperty("Value") val value: String,
)