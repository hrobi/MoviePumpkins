package net.moviepumpkins.core.feature.user

enum class UserRole {
    REVIEWER,
    SUPERVISOR,
    ADMIN;

    companion object {
        val default = REVIEWER
    }

}
