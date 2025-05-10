package net.moviepumpkins.core.oauth

class BearerToken(private val token: String) {
    override fun toString(): String = "Bearer $token"
}